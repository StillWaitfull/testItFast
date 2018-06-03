package common;

public enum Platform {

    PC("pc"), ANDROID("android"), IOS("ios");

    private final String name;

    Platform(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }


}


