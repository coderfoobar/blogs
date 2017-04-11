



import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class TestTask implements Runnable{


    @Override
    public void run() {
        List<Integer> nums = new ArrayList<>();

        for (int i = 0 ; i < 27 ; i++) {
            nums.add(new Integer(i));
        }

        System.out.println("====================================================");

        int pageIndex = 0;
        int pageSize  = 5 ;


        ExecutorService exe = Executors.newFixedThreadPool(3);

        int currPageSize = 0;

        do{
            int offset = pageIndex * pageSize ;
            int toIndex = offset + pageSize;

            if (nums.size() > 0 && toIndex > nums.size()){
                toIndex = nums.size();
            }

            List<Integer> pageNums = nums.subList(offset,toIndex);
            currPageSize = pageNums.size();

            exe.execute(new Runnable() {
                public void run() {

                    for (Integer num : pageNums) {
                        System.out.println(Thread.currentThread().getName() + " --> " + num);
                    }

                    try {
                        Thread.sleep(2000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            });

            pageIndex++;
        }while (currPageSize == pageSize);

        exe.shutdown();

        while (true) {
            if (exe.isTerminated()){
                System.out.println("######################################" + exe + " --> " + exe.isShutdown());
                break;
            }
        }
    }
}
