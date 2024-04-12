package net.fze.util;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class ReflectUtilsTest {

    public class CopyClass {
         String name;
         int age;
         public String getName() {
             return name;
         }
         public void setName(String name) {
             this.name = name;
         }
    }
    public class CopyClass2 extends CopyClass {
        String name2;
        public String getName2() {
            return name2;
        }
        public void setName2(String name2) {
            this.name2 = name2;
        }
    }
    @Test
    void copyObject() {
        CopyClass copyClass = new CopyClass();
        copyClass.setName("test");
        CopyClass2 copyClass2 = new CopyClass2();
        ReflectUtils.copyObject(copyClass,copyClass2);
        copyClass2.setName2("test2");
        System.out.println(Types.toJson(copyClass2));
    }
}