// -----------------------------------------------------------------------------
// SimpleSerialization.java
// -----------------------------------------------------------------------------

/*
 * =============================================================================
 * Copyright (c) 1998-2005 Jeffrey M. Hunter. All rights reserved.
 * 
 * All source code and material located at the Internet address of
 * http://www.idevelopment.info is the copyright of Jeffrey M. Hunter, 2005 and
 * is protected under copyright laws of the United States. This source code may
 * not be hosted on any other site without my express, prior, written
 * permission. Application to host any of the material elsewhere can be made by
 * contacting me at jhunter@idevelopment.info.
 *
 * I have made every effort and taken great care in making sure that the source
 * code and other content included on my web site is technically accurate, but I
 * disclaim any and all responsibility for any loss, damage or destruction of
 * data or any other property which may arise from relying on it. I will in no
 * case be liable for any monetary damages arising from such loss, damage or
 * destruction.
 * 
 * As with any code, ensure to test this code in a development environment 
 * before attempting to run it in production.
 * =============================================================================
 */

import java.util.Hashtable;
import java.util.Enumeration;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.FileOutputStream;
import java.io.FileInputStream;
import java.io.ObjectOutputStream;
import java.io.ObjectInputStream;

/**
 * -----------------------------------------------------------------------------
 * This program demonstrates how to write an application that saves the data
 * content of an arbitrary object by use of Java Object Serialization. In its
 * simplest form, object serialization is an automatic way to save and load
 * the state of an object. Basically, an object of any class that implements
 * the Serialization interface can be saved and restored from a stream. Special
 * stream subclasses, "ObjectInputStream" and "ObjectOutputStream", are used to
 * serialize primitive types and objects. Subclasses of Serializable classes are
 * also serializable. The default serialization mechanism saves the value of an
 * object's nonstatic and nontransient member variables.
 *
 * One of the most important (and tricky) aspects about serialization is that
 * when an object is serialized, any object references it contains are also
 * serialized. Serialization can capture entire "graphs" of interconnected
 * objects and put them back together on the receiving end. The implication is
 * that any object we serialize must contain only references to other 
 * Serializable objects. We can take control of marking nonserializable
 * members as transient or overriding the default serialization mechanisms. The
 * transient modifier can be applied to any instance variable to indicate that
 * its contents are not useful outside of the current context and should never
 * be saved.
 *
 * In the following example, we create a Hashtable and write it to a disk file
 * called HTExample.ser. The Hashtable object is serializable because it 
 * implements the Serializable interface.
 *
 * The doLoad method, reads the Hashtable from the HTExample.ser file, using 
 * the readObject() method of ObjectInputStream. The ObjectInputStream class
 * is a lot like DataInputStream, except that it includes the readObject()
 * method. The return type of readObject() is Object, so we will need to cast
 * it to a Hashtable.
 *
 * @version 1.0
 * @author  Jeffrey M. Hunter  (jhunter@idevelopment.info)
 * @author  http://www.idevelopment.info
 * -----------------------------------------------------------------------------
 */

public class SimpleSerialization {

    /**
     * Create a simple Hashtable and serialize it to a file called
     * HTExample.ser.
     */
    private static void doSave() {

        System.out.println();
        System.out.println("+------------------------------+");
        System.out.println("| doSave Method                |");
        System.out.println("+------------------------------+");
        System.out.println();

        Hashtable h = new Hashtable();
        h.put("string", "Oracle / Java Programming");
        h.put("int", new Integer(36));
        h.put("double", new Double(Math.PI));

        try {

            System.out.println("Creating File/Object output stream...");

            FileOutputStream fileOut = new FileOutputStream("HTExample.ser");
            ObjectOutputStream out = new ObjectOutputStream(fileOut);

            System.out.println("Writing Hashtable Object...");
            out.writeObject(h);

            System.out.println("Closing all output streams...\n");
            out.close();
            fileOut.close();

        } catch(FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    /**
     * Loads the contents of a previously serialized object from a file called
     * HTExample.ser.
     */
    private static void doLoad() {

        System.out.println();
        System.out.println("+------------------------------+");
        System.out.println("| doLoad Method                |");
        System.out.println("+------------------------------+");
        System.out.println();

        Hashtable h = null;


        try {

            System.out.println("Creating File/Object input stream...");

            FileInputStream fileIn = new FileInputStream("HTExample.ser");
            ObjectInputStream in = new ObjectInputStream(fileIn);

            System.out.println("Loading Hashtable Object...");
            h = (Hashtable)in.readObject();

            System.out.println("Closing all input streams...\n");
            in.close();
            fileIn.close();

        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch(FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        System.out.println("Printing out loaded elements...");
        for (Enumeration e = h.keys(); e.hasMoreElements(); ) {
            Object obj = e.nextElement();
            System.out.println("  - Element(" + obj + ") = " + h.get(obj));
        }
        System.out.println();

    }


    /**
     * Sole entry point to the class and application.
     * @param args Array of String arguments.
     */
    public static void main(String[] args) {
        doSave();
        doLoad();
    }

}