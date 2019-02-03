/*
 * Copyright 2003-2012 the original author or authors.
 * 
 * Modified 2016 by Dimitry Polivaev for Freeplane
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.freeplane.plugin.script;

import java.io.File;
import java.io.IOException;
import java.security.*;

import org.codehaus.groovy.control.CompilationFailedException;
import org.codehaus.groovy.control.CompilationUnit;
import org.codehaus.groovy.control.CompilerConfiguration;
import org.codehaus.groovy.control.SourceUnit;
import org.codehaus.groovy.runtime.DefaultGroovyMethods;
import org.codehaus.groovy.runtime.InvokerHelper;

import groovy.lang.*;

/**
 * Represents a groovy shell capable of running arbitrary groovy scripts
 *
 * @author <a href="mailto:james@coredevelopers.net">James Strachan</a>
 * @author Guillaume Laforge
 * @author Paul King
 * @version $Revision$
 */
class GroovyShell extends GroovyObjectSupport {
	static {
		DefaultGroovyMethods.mixin(Number.class, NodeArithmeticsCategory.class);
	}

	private static final String DEFAULT_CODE_BASE = "/groovy/shell";
	private final Binding binding;
	private int counter;
	private final CompilerConfiguration config;
	private final GroovyClassLoader loader;

	GroovyShell(final ClassLoader parent, final Binding binding, final CompilerConfiguration config) {
		if (binding == null) {
			throw new IllegalArgumentException("Binding must not be null.");
		}
		if (config == null) {
			throw new IllegalArgumentException("Compiler configuration must not be null.");
		}
		final ClassLoader parentLoader = (parent != null) ? parent : GroovyShell.class.getClassLoader();
		loader = AccessController.doPrivileged(new PrivilegedAction<GroovyClassLoader>() {
			@Override
			public GroovyClassLoader run() {
				return new MyGroovyClassLoader(parentLoader, config);
			}
		});
		this.binding = binding;
		this.config = config;
	}


	@Override
	public Object getProperty(final String property) {
		Object answer = getVariable(property);
		if (answer == null) {
			answer = super.getProperty(property);
		}
		return answer;
	}

	@Override
	public void setProperty(final String property, final Object newValue) {
		setVariable(property, newValue);
		try {
			super.setProperty(property, newValue);
		}
		catch (final GroovyRuntimeException e) {
			// ignore, was probably a dynamic property
		}
	}

	private Object getVariable(final String name) {
		return binding.getVariables().get(name);
	}

	private void setVariable(final String name, final Object value) {
		binding.setVariable(name, value);
	}

	/**
	 * Parses the groovy code contained in codeSource and returns a java class.
	 */
	private Class parseClass(final GroovyCodeSource codeSource) throws CompilationFailedException {
		// Don't cache scripts
		return loader.parseClass(codeSource, false);
	}

	/**
	 * Parses the given script and returns it ready to be run.  When running in a secure environment
	 * (-Djava.security.manager) codeSource.getCodeSource() determines what policy grants should be
	 * given to the script.
	 *
	 * @param codeSource
	 * @return ready to run script
	 */
	private Script parse(final GroovyCodeSource codeSource) throws CompilationFailedException {
		return InvokerHelper.createScript(parseClass(codeSource), binding);
	}

	/**
	 * Parses the given script and returns it ready to be run
	 *
	 * @param file is the file of the script (which is used to create the class name of the script)
	 */
	Script parse(final File file) throws CompilationFailedException, IOException {
		return parse(new GroovyCodeSource(file, config.getSourceEncoding()));
	}

	/**
	 * Parses the given script and returns it ready to be run
	 *
	 * @param scriptText the text of the script
	 */
	Script parse(final String scriptText) throws CompilationFailedException {
		return parse(scriptText, generateScriptName());
	}

	private Script parse(final String scriptText, final String fileName) throws CompilationFailedException {
		final GroovyCodeSource gcs = AccessController.doPrivileged(new PrivilegedAction<GroovyCodeSource>() {
			@Override
			public GroovyCodeSource run() {
				return new GroovyCodeSource(scriptText, fileName, DEFAULT_CODE_BASE);
			}
		});
		return parse(gcs);
	}

	protected synchronized String generateScriptName() {
		return "Script" + (++counter) + ".groovy";
	}
}

class MyGroovyClassLoader extends GroovyClassLoader {
	MyGroovyClassLoader(final ClassLoader loader, final CompilerConfiguration config) {
		super(loader, config);
	}

	class MyInnerLoader extends InnerLoader {
		private final MyGroovyClassLoader delegate;

		MyInnerLoader(final MyGroovyClassLoader delegate) {
			super(delegate);
			this.delegate = delegate;
		}

		@Override
		protected PermissionCollection getPermissions(final CodeSource codeSource) {
			return delegate.getPermissions(codeSource);
		}
	}

	@Override
	protected PermissionCollection getPermissions(final CodeSource codeSource) {
		final PermissionCollection perms = new Permissions();
		perms.setReadOnly();
		return perms;
	}
	/**
	 * creates a ClassCollector for a new compilation.
	 *
	 * @param unit the compilationUnit
	 * @param su   the SourceUnit
	 * @return the ClassCollector
	 */
	@Override
	protected ClassCollector createCollector(final CompilationUnit unit, final SourceUnit su) {
		final InnerLoader loader = AccessController.doPrivileged(new PrivilegedAction<InnerLoader>() {
			@Override
			public InnerLoader run() {
				return new MyInnerLoader(MyGroovyClassLoader.this);
			}
		});
		return new ClassCollector(loader, unit, su) {
			// use inner class to call protected constructor
		};
	}
}

