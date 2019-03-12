/**
 *
 *  @author Shkambara Dmytro S15163
 *
 */

package zad1;


public class Main {

  public static void main(String[] args) {
	  try{

			new Thread()

			{

				public void run() {

					 new Server(8457);

				}

			}.start();



			Thread.sleep(1000);



			new Thread()

			{

				public void run() {

					GUI gui = new GUI();
					gui.LogWindow();

				}

			}.start();
			new Thread()

			{

				public void run() {

					GUI gui = new GUI();
					gui.LogWindow();

				}

			}.start();
			
			

		}catch(Exception ex){

			ex.printStackTrace();

		}
	 
			
  }
}
