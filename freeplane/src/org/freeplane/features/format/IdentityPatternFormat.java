package org.freeplane.features.format;

import org.freeplane.core.util.TextUtils;

class IdentityPatternFormat extends PatternFormat {
	public IdentityPatternFormat() {
		super(IDENTITY_PATTERN, TYPE_IDENTITY);
	}

	@Override
	public String getStyle() {
		return STYLE_FORMATTER;
	}

    @Override
    public Object formatObject(Object toFormat) {
        if (toFormat instanceof IFormattedObject)
            return ((IFormattedObject) toFormat).getObject();
        return toFormat;
    }

    @Override
    public String toString() {
        return TextUtils.getText(IDENTITY_PATTERN);
    }
}