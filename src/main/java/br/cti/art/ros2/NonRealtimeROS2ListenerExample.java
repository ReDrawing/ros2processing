package br.cti.art.ros2;
import us.ihmc.pubsub.DomainFactory.PubSubImplementation;
import us.ihmc.ros2.ROS2Node;

import java.io.IOException;

/**
 * Java version of the ROS2 demo listener.
 * 
 * To test, start a ROS2 talker using 
 * 
 *    ros2 run demo_nodes_cpp talker -- -t chatter
 * 
 * @author Jesper Smith
 *
 */
public class NonRealtimeROS2ListenerExample
{
   public static void main(String[] args) throws IOException, InterruptedException
   {
      ROS2Node node = new ROS2Node(PubSubImplementation.FAST_RTPS, "NonRealtimeROS2ChatterExample");
      node.createSubscription(new std_msgs.msg.dds.StringPubSubType(), subscriber -> {
         std_msgs.msg.dds.String message = new std_msgs.msg.dds.String();
         if (subscriber.takeNextData(message, null))
         {
            System.out.println(message.getData());
         }
      }, "/chatter");

      Thread.currentThread().join(); // keep thread alive to receive more messages
   }
}
