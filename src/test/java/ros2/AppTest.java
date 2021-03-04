package ros2;

import static org.junit.Assert.assertTrue;

import org.junit.Test;

import br.cti.art.ros2.IMUSubscriber;

import java.io.IOException;

/**
 * Unit test for simple App.
 */
public class AppTest 
{
    /**
     * Rigorous Test :-)
     */
    @Test
    public void shouldAnswerWithTrue()
    {
        assertTrue( true );
    }

    public static void main(String[] args) throws InterruptedException, IOException
    {
      
      IMUSubscriber sub = new IMUSubscriber("/imu");
        
    }
}



