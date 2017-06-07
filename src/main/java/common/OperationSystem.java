package common;

public enum OperationSystem {

    WIN {
        @Override
        protected boolean detect(String osName) {
            if (osName.contains("Windows")) {
                this.set64bit(System.getenv("ProgramFiles(x86)") != null);
                this.executableSuffix = ".exe";
                return true;
            }
            return false;
        }
    },
    MAC_OS {
        @Override
        protected boolean detect(String osName) {
            if (osName.contains("Mac")) {
                this.set64bit(System.getProperty("os.arch").contains("64"));
                this.executableSuffix = ".osx";
                return true;
            }
            return false;
        }
    },
    LINUX {
        @Override
        protected boolean detect(String osName) {
            if (!osName.contains("Mac") && !osName.contains("Windows")) {
                this.set64bit(System.getProperty("os.arch").contains("64"));
                this.executableSuffix = "";
                return true;
            }
            return false;
        }
    };

    public static final OperationSystem instance = OperationSystem.detectOS();
    private String executableSuffix = "";
    private boolean is64bit;
    private String osName;

    private static OperationSystem detectOS() {
        final String osName = System.getProperty("os.name");
        for (OperationSystem operationSystem : OperationSystem.values()) {
            if (operationSystem.detect(osName)) {
                operationSystem.setOsName(osName);
                return operationSystem;
            }
        }
        throw new AssertionError("Operation systems is not detected!");
    }

    protected abstract boolean detect(String osName);

    public boolean is64bit() {
        return is64bit;
    }

    void set64bit(boolean is64bit) {
        this.is64bit = is64bit;
    }

    public boolean isLinux() {
        return !osName.contains("Mac") && !osName.contains("Windows");
    }

    public String getOsName() {
        return this.osName;
    }

    private void setOsName(String name) {
        this.osName = name;
    }

    public String getExecutableSuffix() {
        return executableSuffix;
    }
}
