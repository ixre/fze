package net.fze.ext.report;

public enum XmlResolver {
    Beans(1),
    JAXB(2)
    ;
    private final int value;

    private XmlResolver(int value){
        this.value = value;
    }
}
