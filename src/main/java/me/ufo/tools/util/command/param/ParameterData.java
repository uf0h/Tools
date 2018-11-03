package me.ufo.tools.util.command.param;

public final class ParameterData {

    private String name;
    private boolean wildcard;
    private String defaultValue;
    private String[] tabCompleteFlags;
    private Class<?> parameterClass;

    public ParameterData(Parameter parameterAnnotation, Class<?> parameterClass) {
        this.name = parameterAnnotation.name();
        this.wildcard = parameterAnnotation.wildcard();
        this.defaultValue = parameterAnnotation.defaultValue();
        this.tabCompleteFlags = parameterAnnotation.tabCompleteFlags();
        this.parameterClass = parameterClass;
    }

    public String getName() {
        return name;
    }

    public boolean isWildcard() {
        return wildcard;
    }

    public String getDefaultValue() {
        return defaultValue;
    }

    public String[] getTabCompleteFlags() {
        return tabCompleteFlags;
    }

    public Class<?> getParameterClass() {
        return parameterClass;
    }
}