package net.java.dict4j;

import java.util.List;

import net.java.dict4j.data.Definition;
import net.java.dict4j.data.Dictionary;
import net.java.dict4j.data.Strategy;

public class SocketTest {

    public static void main(String[] args) throws Exception {

        DictClient client = new DictClient();

        System.out.println("------------------------");
        
        List<Dictionary> l = client.getDictionnaries();

        int i = 0;
        for (Dictionary s : l) {
            System.out.println((++i) + " > " + s);
        }
        
        System.out.println("------------------------");

        List<Strategy> sl = client.getStrategies();

        i = 0;
        for (Strategy s : sl) {
            System.out.println((++i) + " > " + s);
        }
        
        System.out.println("------------------------");
        
        List<Definition> dl = client.define(l.get(0), "orange");

        i = 0;
        for (Definition s : dl) {
            System.out.println((++i) + " > " + s);
        }
        
        System.out.println("------------------------");
        
        List<String> ml = client.match(l.get(0), sl.get(10), "orange");

        i = 0;
        for (String s : ml) {
            System.out.println((++i) + " > " + s);
        }
      
        System.out.println("------------------------");
        
        String di = client.getDictionaryInformation(l.get(0));
        System.out.println(di);
      
        System.out.println("------------------------");
        
        String si = client.getServerInformation();
        System.out.println(si);
      
        System.out.println("------------------------");
        
        String st = client.getServerStatus();
        System.out.println(st);
      
        System.out.println("------------------------");

        client.close();
    }
}
