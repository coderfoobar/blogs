
# 两个父子级的List映射成TreeMap

两个List，包含相同的实体，但是数据呈现为父对子、一对多的关系，将两个List合并为一个数据结构，映射成一个TreeMap

```Java

import org.junit.Test;
import java.util.*;

public class TestMap {

    @Test
    public void test(){
        User top1 = new User("0","1");
        User top2 = new User("0","2");
        User top3 = new User("0","3");

        List<User> topList = new ArrayList<>();
        topList.add(top1);
        topList.add(top2);
        topList.add(top3);

        User sec1 = new User("1","a");
        User sec2 = new User("1","b");
        User sec3 = new User("1","c");
        User sec4 = new User("2","d");
        User sec5 = new User("2","e");
        User sec6 = new User("3","f");
        User sec7 = new User("3","g");
        User sec8 = new User("3","h");
        List<User> secList = new ArrayList<>();
        secList.add(sec1);
        secList.add(sec2);
        secList.add(sec3);
        secList.add(sec4);
        secList.add(sec5);
        secList.add(sec6);
        secList.add(sec7);
        secList.add(sec8);

        TreeMap<User , List<User>> userMap = new TreeMap<>();

        int j = 0;
        if (topList.size() > 0 ) {
            User top = topList.get(j);

            for (int i = 0 ; i < secList.size() ; ) {
                User sec = secList.get(i);
                if (top.getF().equals(sec.getP())) {
                    List<User> users = userMap.get(top);
                    if (null == users) {
                        users = new ArrayList<>();
                        userMap.put(top , users);
                    }
                    users.add(sec);
                    i++;
                }else {
                    j++;
                    if (j < topList.size()) {
                        top = topList.get(j);
                    }
                }

            }
        }

        System.out.println(userMap);
    }

    class User implements Comparable{
        /**
         * parent factor 
         **/
        String p;

        /**
         * factor 
         **/
        String f;

        public User(String p, String f) {
            this.p = p;
            this.f = f;
        }

        public String getP() {
            return p;
        }

        public void setP(String p) {
            this.p = p;
        }

        public String getF() {
            return f;
        }

        public void setF(String f) {
            this.f = f;
        }

        @Override
        public String toString() {
            return "User{" +
                    "p='" + p + '\'' +
                    ", f='" + f + '\'' +
                    '}';
        }

        @Override
        public int compareTo(Object o) {
            return this.getF().compareTo(((User)o).getF());
        }
    }
}


```