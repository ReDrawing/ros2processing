package br.cti.art.ros2;

import sensor_msgs.msg.dds.Image;
import sensor_msgs.msg.dds.ImagePubSubType;

import org.opencv.core.Mat;

public class ImageSubscriber implements Runnable
{
    private Thread thread;
    private String topicName;

    public ImageSubscriber(String topicName)
    {
        this.topicName = topicName;

        thread = new Thread(this);
        
        thread.start();
    }

    @Override
    public void run() 
    {
        try
        {
            Node node = Node.getInstance();
            node.createSubscription(new ImagePubSubType(), subscriber -> {
                Image message = new Image();
                if (subscriber.takeNextData(message, null)) {
                    System.out.println("aaa");
                    System.out.println(message.getData().toArray()[0]);
                }
            }, topicName);
            Thread.currentThread().join();
        }
        catch (Exception e)
        {
            ///@todo Tratar exceções
            System.out.println("Error");
        }
        
    }
    
}
