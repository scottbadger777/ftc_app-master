package org.engine;

import java.util.Enumeration;
import java.util.Hashtable;

/**
 * Created by t420 on 1/27/2017.
 */
//
public class Cont {

    Hashtable hashtable = new Hashtable();

    public static enum types {
        DCMOTOR,SERVO,CONTINUOUSSERVO,I2C,TOUCH,COLOR,OPTICALDISTANCE
    }


    public void put(types type, String name, Object object){
        hashtable.put(type+","+name, object);
    }

    public Object get(types type, String name){

        return hashtable.get(type+","+name);
    }

    public Object[] getInType(types type){

        Enumeration enumeration = hashtable.keys();

        Object[] output = new Object[100];
        Object[] keys= new Object[100];

        int y = 0;


        while (enumeration.hasMoreElements()){

            System.out.print(enumeration.nextElement());


        }



        return null;

    }

    public void clear(){
        hashtable.clear();
    }

}
