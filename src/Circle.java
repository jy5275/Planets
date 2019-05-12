import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.geom.*;
import javax.swing.*;

public class Circle extends JButton {
    public Circle(String label) {
        super(label);
        this.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(MouseEvent e) {
                ((JButton) e.getSource()).setCursor(new Cursor(Cursor.HAND_CURSOR));
            }

            public void mouseExited(MouseEvent e) {
                ((JButton) e.getSource()).setCursor(new Cursor(Cursor.MOVE_CURSOR));
            }
        });
        // 这些声明把按钮扩展为一个圆而不是一个椭圆。
        Dimension size = getPreferredSize();
        size.width = size.height = Math.max(size.width, size.height);
        size.width = 200;
        size.height = 400;
        setPreferredSize(size);

        // 这个调用使JButton不画背景，而允许我们画一个圆的背景。
        setContentAreaFilled(false);
    }

    // 画圆的背景和标签
    protected void paintComponent(Graphics g) {
        if (getModel().isArmed()) {
            // 你可以选一个高亮的颜色作为圆形按钮类的属性
            g.setColor(Color.lightGray);
            System.out.println("Click");
        } else {
            g.setColor(getBackground());
            System.out.println("Peace");
        }
        g.fillOval(0, 0, getSize().width - 1, getSize().height - 1);
        // 这个调用会画一个标签和焦点矩形。
        super.paintComponent(g);
    }

    // 侦测点击事件
    Shape shape;

    public boolean contains(int x, int y) {
        // 如果按钮改变大小，产生一个新的形状对象。
        if (shape == null || !shape.getBounds().equals(getBounds()))
            shape = new Ellipse2D.Float(0, 0, getWidth(), getHeight());
        return shape.contains(x, y);
    }

    // 测试程序
    public static void main(String[] args) {
        // 产生一个带‘Jackpot'标签的按钮。
        JButton button = new Circle("Jackpot");
        button.setBackground(Color.green);
        // 产生一个框架以显示这个按钮。
        Frame frame = new Frame();
        frame.setBackground(Color.yellow);
        frame.add(button);
        // frame.setLayout(new FlowLayout());
        frame.setSize(550, 550);
        frame.setVisible(true);
    }
}