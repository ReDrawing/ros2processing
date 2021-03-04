package br.cti.art.ros2;

import sensor_msgs.msg.dds.Imu;
import sensor_msgs.msg.dds.ImuPubSubType;


/**
 * Encapsula e simplifica o recebimento de mensagens de IMU
 */
public class IMUSubscriber implements Runnable
{
    private Thread thread;
    private String topicName;

    private double[] orientation;
    private double[] angle;
    private double[] accel;
    private double[] gyro;
    private double time;

    private boolean newMessage;

    /**
     * Construtor de IMUSubscriber
     * 
     * @param topicName - Nome completo do tópico a ser inscrito
     */
    public IMUSubscriber(String topicName)
    {
        this.topicName = topicName;
        
        orientation = new double[4];
        angle = new double[3];
        accel = new double[3];
        gyro = new double[3];
        time = 0;

        newMessage = false;

        thread = new Thread(this);
        thread.start();
    }

    /**
     * Recebe os dados da mensagem
     */
    public void run() 
    {
        try
        {
            Node node = Node.getInstance();
            node.createSubscription(new ImuPubSubType(), subscriber -> {
                Imu message = new Imu();
                if (subscriber.takeNextData(message, null)) {
                    orientation[0] = message.getOrientation().getX();
                    orientation[1] = message.getOrientation().getY();
                    orientation[2] = message.getOrientation().getZ();
                    orientation[3] = message.getOrientation().getS();

                    accel[0] = message.getLinearAcceleration().getX();
                    accel[1] = message.getLinearAcceleration().getY();
                    accel[2] = message.getLinearAcceleration().getZ();

                    gyro[0] = message.getAngularVelocity().getX();
                    gyro[1] = message.getAngularVelocity().getY();
                    gyro[2] = message.getAngularVelocity().getZ();

                    time = message.getHeader().getStamp().getSec();
                    time += (0.000000001)*message.getHeader().getStamp().getNanosec();

                    newMessage = true;
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

    /**
     * Verifica se existe uma nova mensagem
     * 
     * O estado é atualizado ao receber uma nova mensagem ou ao acessar
     * algum dado da última mensagem
     * 
     * @return True se houver uma nova mensagem
     */
    public boolean hasNewMessage()
    {
        return newMessage;
    }

    /**
     * Retorna a velocidade angular da mensagem 
     * 
     * @return vetor da velocidade nos eixos [x, y, z], em rad/s
     */
    public double[] getGyro()
    {
        newMessage = false;

        return this.gyro;
    }

    /**
     * Retorna a aceleração linear da mensagem 
     * 
     * @return vetor da aceleração nos eixos [x, y, z], em m/s^2
     */
    public double[] getAccel()
    {
        newMessage = false;

        return this.accel;
    }

    /**
     * Retorna a orientação da mensagem, na forma quaternária
     * 
     * @return quaternion [x, y, z, w]
     */
    public double[] getOrientation()
    {
        newMessage = false;

        return this.orientation;
    }

    /**
     * Retorna a orientação da mensagem, em ângulos euclidianos
     * 
     * @return ângulo nos eixos [x, y, z], em rad
     */
    public double[] getAngle()
    {
        newMessage = false;

        computeAngle();

        return this.angle;
    }

    public double getTime()
    {
        newMessage = false;

        return time;
    }

    /**
     * Converte orientation (quaternion) para angle (euclidiano)
     * 
     * Equações retiradas de https://en.wikipedia.org/wiki/Conversion_between_quaternions_and_Euler_angles
     */
    private void computeAngle()
    {
        double q0 = orientation[3];
        double q1 = orientation[0];
        double q2 = orientation[1];
        double q3 = orientation[2];

        angle[0] = Math.atan2(2*((q0*q1)+(q2*q3)) , 1-2*(Math.pow(q1,2)+Math.pow(q2,2)  )  );
        angle[1] = Math.asin(2*((q0*q2) - (q3*q1)) );
        angle[2] = Math.atan2(2*((q0*q3)+(q1*q2)) , 1-2*(Math.pow(q2,2)+Math.pow(q3,2)  )  );
    }
    
}
