//Понять, почему не работает!

//package gui;
//
//import java.awt.Color;
//import java.awt.EventQueue;
//import java.awt.Graphics;
//import java.awt.Graphics2D;
//import java.awt.Point;
//import java.awt.event.MouseAdapter;
//import java.awt.event.MouseEvent;
//import java.awt.geom.AffineTransform;
//import java.util.Timer;
//import java.util.TimerTask;
//
//import javax.swing.JPanel;
//
//import static gui.Robot.maxAngularVelocity;
//import static gui.Robot.maxVelocity;
//
//public class GameVisualizer extends JPanel {
//    private final Timer m_timer = initTimer();
//
//    private static Timer initTimer() {
//        return new Timer("events generator", true);
//    }
//
//    private volatile Robot robot = new Robot(100, 100, 0);
//
//    private volatile int m_targetPositionX = 150;
//    private volatile int m_targetPositionY = 100;
//
//    public GameVisualizer() {
//        m_timer.schedule(new TimerTask() {
//            @Override
//            public void run() {
//                onRedrawEvent();
//            }
//        }, 0, 50);
//        m_timer.schedule(new TimerTask() {
//            @Override
//            public void run() {
//                onModelUpdateEvent();
//            }
//        }, 0, 10);
//        addMouseListener(new MouseAdapter() {
//            @Override
//            public void mouseClicked(MouseEvent e) {
//                setTargetPosition(e.getPoint());
//                repaint();
//            }
//        });
//        setDoubleBuffered(true);
//    }
//
//    protected void setTargetPosition(Point p) {
//        m_targetPositionX = p.x;
//        m_targetPositionY = p.y;
//    }
//
//    protected void onRedrawEvent() {
//        EventQueue.invokeLater(this::repaint);
//    }
//
//    private static double distance(double x1, double y1, double x2, double y2) {
//        double diffX = x1 - x2;
//        double diffY = y1 - y2;
//        return Math.sqrt(diffX * diffX + diffY * diffY);
//    }
//
//    private static double angleTo(double fromX, double fromY, double toX, double toY) {
//        double diffX = toX - fromX;
//        double diffY = toY - fromY;
//
//        return asNormalizedRadians(Math.atan2(diffY, diffX));
//    }
//
//    protected void onModelUpdateEvent() {
//        double distance = distance(m_targetPositionX, m_targetPositionY,
//                robot.getX(), robot.getY());
//        if (distance < 0.5) {
//            return;
//        }
//        double angleToTarget = angleTo(robot.getX(), robot.getY(), m_targetPositionX, m_targetPositionY);
//        double angularVelocity = 0;
//        double angle = asNormalizedRadians(angleToTarget - robot.getDirection());
//
//        angularVelocity = angleToTarget > robot.getDirection() ? maxAngularVelocity : -maxAngularVelocity;
//
//        moveRobot(maxVelocity, angularVelocity, 10, angle);
//    }
//
//    private static double applyLimits(double value, double min, double max) {
//        return Math.min(Math.max(value, min), max);
//    }
//
//    private void moveRobot(double velocity, double angularVelocity, double duration, double angle) {
//        velocity = applyLimits(velocity, 0, maxVelocity);
//        angularVelocity = applyLimits(angularVelocity, maxAngularVelocity, maxAngularVelocity);
//
//
//        double newX = robot.getX() + velocity / angularVelocity *
//                (Math.sin(robot.getDirection()  + Math.min(angle, angularVelocity) * duration) -
//                        Math.sin(robot.getDirection()));
//        if (!Double.isFinite(newX))
//        {
//            newX = robot.getX() + velocity * duration * Math.cos(robot.getDirection());
//        }
//        double newY = getNewY(velocity, angularVelocity, angle, duration);
//
//        double newDirection = asNormalizedRadians(robot.getDirection() + Math.min(angle, angularVelocity) * duration);
//        robot.updatePosition(newX, newY, newDirection);
//    }
//
//
//    private double getNewY(double velocity, double angularVelocity, double angle, double duration) {
//        double direction = Math.cos(robot.getDirection());
//        double direction2 = Math.cos(robot.getDirection() + Math.min(angle, angularVelocity) * duration);
//        double value = robot.getY() - velocity / angularVelocity * (direction2 - direction);
//        return Double.isFinite(value) ? value : robot.getY() + velocity * duration * Math.sin(robot.getDirection());
//    }
//
//    private static double asNormalizedRadians(double angle) {
//        return (angle % (2 * Math.PI) + 2 * Math.PI) % (2 * Math.PI);
//    }
//
//    private static int round(double value) {
//        return (int) (value + 0.5);
//    }
//
//    @Override
//    public void paint(Graphics g) {
//        super.paint(g);
//        Graphics2D g2d = (Graphics2D) g;
//        drawRobot(g2d, round(robot.getX()), round(robot.getY()), robot.getDirection());
//        drawTarget(g2d, m_targetPositionX, m_targetPositionY);
//    }
//
//    private static void fillOval(Graphics g, int centerX, int centerY, int diam1, int diam2) {
//        g.fillOval(centerX - diam1 / 2, centerY - diam2 / 2, diam1, diam2);
//    }
//
//    private static void drawOval(Graphics g, int centerX, int centerY, int diam1, int diam2) {
//        g.drawOval(centerX - diam1 / 2, centerY - diam2 / 2, diam1, diam2);
//    }
//
//    private void drawRobot(Graphics2D g, int robotCenterX, int robotCenterY, double direction) {
//        AffineTransform t = AffineTransform.getRotateInstance(direction, robotCenterX, robotCenterY);
//        g.setTransform(t);
//        g.setColor(Color.MAGENTA);
//        fillOval(g, robotCenterX, robotCenterY, 30, 10);
//        g.setColor(Color.BLACK);
//        drawOval(g, robotCenterX, robotCenterY, 30, 10);
//        g.setColor(Color.WHITE);
//        fillOval(g, robotCenterX + 10, robotCenterY, 5, 5);
//        g.setColor(Color.BLACK);
//        drawOval(g, robotCenterX + 10, robotCenterY, 5, 5);
//    }
//
//    private void drawTarget(Graphics2D g, int x, int y) {
//        AffineTransform t = AffineTransform.getRotateInstance(0, 0, 0);
//        g.setTransform(t);
//        g.setColor(Color.GREEN);
//        fillOval(g, x, y, 5, 5);
//        g.setColor(Color.BLACK);
//        drawOval(g, x, y, 5, 5);
//    }
//}

package gui;

import java.awt.Color;
import java.awt.EventQueue;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.geom.AffineTransform;
import java.util.Timer;
import java.util.TimerTask;

import javax.swing.JPanel;

public class GameVisualizer extends JPanel
{
    private final Timer m_timer = initTimer();

    private static Timer initTimer()
    {
        return new Timer("events generator", true);
    }

    //можно создать тип колбаса с её данными в качестве поле, аналогично для точки
    private volatile double m_robotPositionX = 100;
    private volatile double m_robotPositionY = 100; 
    private volatile double m_robotDirection = 0; 

    private volatile int m_targetPositionX = 150;
    private volatile int m_targetPositionY = 100;
    
    private static final double maxVelocity = 0.1;
    private static final double maxAngularVelocity = 0.015;

    private double previousAngle = 0;
    
    public GameVisualizer() 
    {
        m_timer.schedule(new TimerTask()
        {
            @Override
            public void run()
            {
                onRedrawEvent();
            }
        }, 0, 50);
        m_timer.schedule(new TimerTask()
        {
            @Override
            public void run()
            {
                onModelUpdateEvent();
            }
        }, 0, 10);
        addMouseListener(new MouseAdapter()
        {
            @Override
            public void mouseClicked(MouseEvent e)
            {
                setTargetPosition(e.getPoint());
                repaint();
            }
        });
        setDoubleBuffered(true);
    }

    protected void setTargetPosition(Point p)
    {
        m_targetPositionX = p.x;
        m_targetPositionY = p.y;
    }
    
    protected void onRedrawEvent()
    {
        EventQueue.invokeLater(this::repaint);
    }

    private static double distance(double x1, double y1, double x2, double y2)
    {
        double diffX = x1 - x2;
        double diffY = y1 - y2;
        return Math.sqrt(diffX * diffX + diffY * diffY);
    }
    
    private static double angleTo(double fromX, double fromY, double toX, double toY)
    {
        double diffX = toX - fromX;
        double diffY = toY - fromY;
        
        return asNormalizedRadians(Math.atan2(diffY, diffX));
    }
    
    protected void onModelUpdateEvent()
    {
        double distance = distance(m_targetPositionX, m_targetPositionY,
            m_robotPositionX, m_robotPositionY);
        if (distance < 0.5)
        {
            return;
        }
        double velocity = maxVelocity;
        double angleToTarget = angleTo(m_robotPositionX, m_robotPositionY, m_targetPositionX, m_targetPositionY);
        double angularVelocity = 0;
        double angle = asNormalizedRadians(angleToTarget - m_robotDirection);
        double angleDiff = asNormalizedRadians(angle - previousAngle);
        int angleToTargetDegrees = (int) Math.round(Math.toDegrees(angleToTarget));
        if (Math.abs(angleDiff) < 0.1) {
            angularVelocity = 0;
        } else if (angleToTargetDegrees % 45 == 0) {
            angularVelocity = 0;
        } else {
            if (angleToTarget > m_robotDirection) {
                angularVelocity = maxAngularVelocity;
            } else if (angleToTarget < m_robotDirection) {
                angularVelocity = -maxAngularVelocity;
            }
        }

        previousAngle = angle;

        moveRobot(velocity, angularVelocity, 10, angle);

    }
    
    private static double applyLimits(double value, double min, double max)
    {
        return Math.min(Math.max(value, min), max);
    }
    
    private void moveRobot(double velocity, double angularVelocity, double duration, double angle)
    {
        velocity = applyLimits(velocity, 0, maxVelocity);
        angularVelocity = applyLimits(angularVelocity, -maxAngularVelocity, maxAngularVelocity);
        double newX = m_robotPositionX + velocity / angularVelocity * 
            (Math.sin(m_robotDirection  + Math.min(angle, angularVelocity) * duration) -
                Math.sin(m_robotDirection));
        if (!Double.isFinite(newX))
        {
            newX = m_robotPositionX + velocity * duration * Math.cos(m_robotDirection);
        }
        double newY = m_robotPositionY - velocity / angularVelocity * 
            (Math.cos(m_robotDirection  + Math.min(angle, angularVelocity) * duration) -
                Math.cos(m_robotDirection));
        if (!Double.isFinite(newY))
        {
            newY = m_robotPositionY + velocity * duration * Math.sin(m_robotDirection);
        }
        m_robotPositionX = newX;
        m_robotPositionY = newY;
        double newDirection = asNormalizedRadians(m_robotDirection + Math.min(angle, angularVelocity) * duration);
        m_robotDirection = newDirection;
    }

    private static double asNormalizedRadians(double angle)
    {
        return (angle % (2 * Math.PI) + 2 * Math.PI) % (2 * Math.PI);
    }
    
    private static int round(double value)
    {
        return (int)(value + 0.5);
    }
    
    @Override
    public void paint(Graphics g)
    {
        super.paint(g);
        Graphics2D g2d = (Graphics2D)g; 
        drawRobot(g2d, round(m_robotPositionX), round(m_robotPositionY), m_robotDirection);
        drawTarget(g2d, m_targetPositionX, m_targetPositionY);
    }
    
    private static void fillOval(Graphics g, int centerX, int centerY, int diam1, int diam2)
    {
        g.fillOval(centerX - diam1 / 2, centerY - diam2 / 2, diam1, diam2);
    }
    
    private static void drawOval(Graphics g, int centerX, int centerY, int diam1, int diam2)
    {
        g.drawOval(centerX - diam1 / 2, centerY - diam2 / 2, diam1, diam2);
    }
    
    private void drawRobot(Graphics2D g, int robotCenterX, int robotCenterY, double direction)
    {
        AffineTransform t = AffineTransform.getRotateInstance(direction, robotCenterX, robotCenterY);
        g.setTransform(t);
        g.setColor(Color.MAGENTA);
        fillOval(g, robotCenterX, robotCenterY, 30, 10);
        g.setColor(Color.BLACK);
        drawOval(g, robotCenterX, robotCenterY, 30, 10);
        g.setColor(Color.WHITE);
        fillOval(g, robotCenterX  + 10, robotCenterY, 5, 5);
        g.setColor(Color.BLACK);
        drawOval(g, robotCenterX  + 10, robotCenterY, 5, 5);
    }
    
    private void drawTarget(Graphics2D g, int x, int y)
    {
        AffineTransform t = AffineTransform.getRotateInstance(0, 0, 0); 
        g.setTransform(t);
        g.setColor(Color.GREEN);
        fillOval(g, x, y, 5, 5);
        g.setColor(Color.BLACK);
        drawOval(g, x, y, 5, 5);
    }
}
