
public class ThreadPoolExecutorTest {

    public static void main(String[] args) throws Exception {

//        for (int i = 0 ; i < 3 ; i++) {
            new Thread(new TestTask()).start();
//        }



    }
}