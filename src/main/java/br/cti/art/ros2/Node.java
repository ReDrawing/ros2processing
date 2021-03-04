package br.cti.art.ros2;

import java.io.IOException;

import us.ihmc.pubsub.DomainFactory.PubSubImplementation;
import us.ihmc.ros2.ROS2Node;

public class Node extends ROS2Node {
    private static Node instance;

    private Node() throws IOException {
        super(PubSubImplementation.FAST_RTPS, "teste");
    }

    static Node getInstance()
    {
        if(instance == null)
        {
            try
            {
                instance = new Node();
            }
            catch(IOException e)
            {
                ///@todo tratar exceção
            }            
        }

        return instance;
    } 
}
