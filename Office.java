/* *
 *      Author: Guang Peng Li
 *      Date: 10/03/2013
 *      Project: OS Assignment 1
 *      University of Liverpool
 *      Comment: Manipulation of Threads
 *  */

import java.util.concurrent.Semaphore;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
 
public class Office {
    /**############################################**/
    /**----------------------------------- Field ----------------------------------**/
    /**############################################**/
        // Declarations
    /**############################################**/
    /**----------------------------------- Main ----------------------------------**/
    /**############################################**/
    public static void main(String[] args) throws InterruptedException {
        // Set up: Object for job class
        final Job work = new Job();
        // Thread 0: Secretary A
        new Thread(){
            public void run(){
                work.secretaryA();
            }
        }.start();
        // Thread 1: Secretary B
        new Thread(){
            public void run(){
                work.secretaryB();
            }
        }.start();
        // Thread 2: Secretary C
        new Thread(){
            public void run(){
                work.secretaryC();
            }
        }.start();
        // Thread 3: Manager
        new Thread(){
            public void run(){
                work.manager(); 
            }
        }.start();
        
    }
}

class Job{
    /**############################################**/
    /**----------------------------------- Field ----------------------------------**/
    /**############################################**/
    // Decralations Variables
    private final int MAX = 5; 
    private final int MIN = 0;
    private BlockingQueue<Integer> tray = new ArrayBlockingQueue<Integer>(MAX);
    private boolean full = (tray.size()>=MAX);
    private boolean empty = (tray.size()<=MIN);
    Semaphore key = new Semaphore(10,true);
	Object lock = new Object();
    /**############################################**/
    /**---------------------------------- Threads ---------------------------------**/
    /**############################################**/
    // Secretary A
    public void secretaryA() {
		// Insert 7 letters
        for(int i = 1; i <= 7; i++){
            try {
                // Acquires a permit
                key.acquire();
                System.out.println("#Secretary A is ready to type a new letter");
                // Set the thread to sleep for 1 second
                Thread.sleep(1000);
                System.out.printf(" Secretary A has typed letter: [%d]\n",i);
				// Call the insert method
                insert();
                // Check if the tray is full
                full = (tray.size()>=MAX);
                // Tray becomes not empty
                empty = false;
                System.out.printf("-Secretary A has successfully added a letter to the tray : Tray size: [%d]+\n",tray.size());
            } catch (InterruptedException e) {} 
            finally {
                // Call the signal method to wake up a waiting thread
                signal();
                // Releases a permit
                key.release();
            }
        }
    }
    // Secretary B
    public void secretaryB(){
		// Insert 7 letters
        for(int i = 1; i <= 7; i++){
            try {
                // Acquires a permit
                key.acquire();
                System.out.println("#Secretary B is ready to type a new letter");
                // Set the thread to sleep for 2 seconds
                Thread.sleep(2000);
                System.out.printf(" Secretary B has typed letter: [%d]\n",i);
				// Call the insert method
                insert();
                // Check if the tray is full
                full = (tray.size()>=MAX);
                // Tray becomes not empty
                empty = false;
                System.out.printf("-Secretary B has successfully added a letter to the tray : Tray size: [%d]+\n",tray.size());
            } catch (InterruptedException e) {} 
            finally {
                // Call the signal method to wake up a waiting thread
                signal();
                // Releases a permit
                key.release();
            }
        }
    }
    // Secretary C
    public void secretaryC(){
		// Insert 7 letters
        for(int i = 1; i <= 7; i++){
            try {
                // Acquires a permit
                key.acquire();
                System.out.println("#Secretary C is ready to type a new letter");
                // Set the thread to sleep for 4 seconds
                Thread.sleep(4000);
                System.out.printf(" Secretary C has typed letter: [%d]\n",i);
				// Call the insert method
                insert();
                // Check if the tray is full
                full = (tray.size()>=MAX);
                // Tray becomes not empty
                empty = false;
                System.out.printf("-Secretary C has successfully added a letter to the tray : Tray size: [%d]+\n",tray.size());
            } catch (InterruptedException e) {} 
            finally {
                // Call the signal method to wake up a waiting thread
                signal();
                // Releases a permit
                key.release();
            }
        }
    }
    // Manager
    public void manager(){
		// Remove 21 letters
        for(int i = 1; i <= 21; i++){
            try {
                // Acquires a permit
                key.acquire();
                System.out.println("#The Manager is ready to sign a letter");
				// Call the remove method
                remove();
                // Check if the tray is empty
                empty = (tray.size()<=MIN);
                // Tray becomes not full
                full = false;
                System.out.printf("-A letter has been removed from the tray.\t\t : Tray size: [%d]-\n",tray.size());
            } catch (InterruptedException e) {} 
            finally {
                // Call the signal method to wake up a waiting thread
                signal();
                // Releases a permit
                key.release();
                try{
                    // Set the thread to sleep for 2 seconds
                    Thread.sleep(2000);
                } catch(InterruptedException e) {}
                System.out.printf("-The Manager has signed a letter: \t[%d]\n",i);
            }
        }
    }
    /**############################################**/
    /**---------------------------------- Synchronisation ---------------------------------**/
    /**############################################**/
    // Secretaties' wait time and insert letters to the tray
    private void insert() throws InterruptedException{
        // Synchronise the threads
		synchronized(lock){
			// Set the thread to wait when the tray is full
			while(full){
				try{
					lock.wait();
				} catch(InterruptedException e){}
			}
			// Insert a letter into the tray
			tray.put(1);
		}
    }  
    // Manager's wait time and remove letters to the tray
    private void remove() throws InterruptedException{
        // Synchronise the threads
        synchronized(lock){
			// Set the thread to wait when the tray is empty
			while(empty){
				try{
					lock.wait();
				} catch(InterruptedException e){}
			}
			// Remove a letter from the tray
			tray.take();
		}
    }
    // Notification to a threads when the tray is available
    private void signal(){
        // Synchronise the threads
		synchronized(lock){
			// wake up a waiting thread
			lock.notify();
		}
    }
}