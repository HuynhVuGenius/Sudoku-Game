package sodokugamegroup3;

import static com.sun.java.accessibility.util.AWTEventMonitor.addActionListener;
import java.awt.*;
import java.awt.event.*;
import java.io.FileNotFoundException;
import javax.swing.BorderFactory;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JTextField;
import javax.swing.Timer;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Observable;
import java.util.Stack;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.UIManager;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import javax.swing.JPanel;

/**
 *
 * @author Group 3 - SE1703
 */
public class SodokuFrame extends javax.swing.JFrame implements ActionListener, KeyListener, MouseListener {
    MenuJFrame menuJFrame;

    SodokuBank bank = new SodokuBank();
    public static final int numRows = 9;
    public static final int numCols = 9;
    private Cell[][] cells = new Cell[numRows][numCols + 1];
    ;
    private int temp[] = new int[81];
    private int x[] = new int[81];
    private int y[] = new int[81];
    private int[][] a = new int[9][10];
    private int[][] A = new int[9][10];
    private int[][] aa = new int[9][10];
    private int[][] gt = new int[9][10];
    int an[] = {0, 45, 52};
    String str;
    int I, J;
    public Timer timer;
    int LV = 0;
    int NO = 100;
    boolean endDq = false;
    private int selectedNumber;

    /**
     * Returns number selected user.
     *
     * @return Number selected by user.
     */
    public void generateBoard() {

        pnlBoard.setLayout(new GridLayout(numRows, numCols));
        pnlBoard.removeAll();
        pnlBoard.revalidate();
        pnlBoard.repaint();

        cboxLevel.setSelectedIndex(LV);

        lblNotice.setEditable(false);

        for (int i = 0; i < 9; i++) {
            for (int j = 0; j < 9; j++) {
                cells[i][j] = new Cell();
                cells[i][j].addActionListener(this);
                cells[i][j].addKeyListener(this);
                cells[i][j].setActionCommand(i + " " + j);
                cells[i][j].setBackground(Color.white);
                cells[i][j].setFont(new Font("UTM Micra", 1, 30));
                cells[i][j].setForeground(Color.black);
                pnlBoard.add(cells[i][j]);
            }
        }
        for (int i = 0; i < 9; i += 3) {
            for (int j = 0; j < 9; j += 3) {
                cells[i][j].setBorder(BorderFactory.createMatteBorder(3, 3, 1, 1, Color.black));
                cells[i][j + 2].setBorder(BorderFactory.createMatteBorder(3, 1, 1, 3, Color.black));
                cells[i + 2][j + 2].setBorder(BorderFactory.createMatteBorder(1, 1, 3, 3, Color.black));
                cells[i + 2][j].setBorder(BorderFactory.createMatteBorder(1, 3, 3, 1, Color.black));
                cells[i][j + 1].setBorder(BorderFactory.createMatteBorder(3, 1, 1, 1, Color.black));
                cells[i + 1][j + 2].setBorder(BorderFactory.createMatteBorder(1, 1, 1, 3, Color.black));
                cells[i + 2][j + 1].setBorder(BorderFactory.createMatteBorder(1, 1, 3, 1, Color.black));
                cells[i + 1][j].setBorder(BorderFactory.createMatteBorder(1, 3, 1, 1, Color.black));
                cells[i + 1][j + 1].setBorder(BorderFactory.createMatteBorder(1, 1, 1, 1, Color.black));
            }
        }
        numberInputs = new JButton[]{
            j1, j2, j3, j4, j5, j6, j7, j8, j9
        };
    }

    public void clear() {
        this.str = "";
        for (int i = 0; i < 9; i++) {
            for (int j = 0; j < 9; j++) {
                a[i][j] = A[i][j] = 0;
                x[i * j] = 0;
                y[i * j] = 0;
                temp[i * j] = 0;
                cells[i][j].setText("");
                cells[i][j].setForeground(Color.black);
            }
        }
        NO = 100;
        endDq = false;
    }

    public void setValueInInput(int i, int j) {
        if (a[i][j] == 0) {
            cells[i][j].setText(Integer.toString(curNum));
        }
    }

    JButton[] numberInputs = new JButton[9]; // To take user inputs which are 1 to 9 numbers
    int curNum = 1;

    public void selectNumber(int num) {
        int prevNum = curNum;
        JButton prevSelected = numberInputs[prevNum - 1];
        prevSelected.setBackground(new Color(255, 255, 255));
        JButton curSelected = numberInputs[num - 1];
        curSelected.setBackground(new Color(164, 211, 238));
        curNum = num;
        
    }

    public void creatMatrix() throws FileNotFoundException {
        str = bank.getBank()[(int) ((bank.getBankSize() - 1) * Math.random()) + 1];
        int N = 0;
        for (int i = 0; i < 9; i++) {
            for (int j = 0; j < 9; j++) {
                a[i][j] = A[i][j] = str.charAt(i * 9 + j) - 48;
            }
        }

        for (int i = 0; i < 9; i++) {
            for (int j = 0; j < 9; j++) {
                x[N] = i;
                y[N] = j;
                temp[N++] = (int) (10000 * Math.random());
            }
        }
        for (int i = 0; i < N - 1; i++) {
            for (int j = i + 1; j < N; j++) {
                if (temp[i] > temp[j]) {
                    int t = x[i];
                    x[i] = x[j];
                    x[j] = t;

                    t = y[i];
                    y[i] = y[j];
                    y[j] = t;

                    t = temp[i];
                    temp[i] = temp[j];
                    temp[j] = t;
                }
            }
        }
        for (int i = 0; i < an[LV]; i++) {
            a[x[i]][y[i]] = 0;
        }
        for (int i = 0; i < 9; i++) {
            for (int j = 0; j < 9; j++) {
                if (a[i][j] > 0) {
                    cells[i][j].setText(a[i][j] + "");
                }
            }
        }
        for (int i = 0; i < 9; i++) {
            for (int j = 0; j < 9; j++) {
                aa[i][j] = a[i][j];
            }
        }
    }

    public String next(JTextField txt) {
        String str[] = txt.getText().split(":");
        int tt = Integer.parseInt(str[3]);
        int s = Integer.parseInt(str[2]);
        int m = Integer.parseInt(str[1]);
        int h = Integer.parseInt(str[0]);
        String kq = "";
        int sum = tt + s * 100 + m * 60 * 100 + h * 60 * 60 * 100 + 1;
        if (sum % 100 > 9) {
            kq = ":" + sum % 100 + kq;
        } else {
            kq = ":0" + sum % 100 + kq;
        }
        sum /= 100;

        if (sum % 60 > 9) {
            kq = ":" + sum % 60 + kq;
        } else {
            kq = ":0" + sum % 60 + kq;
        }
        sum /= 60;

        if (sum % 60 > 9) {
            kq = ":" + sum % 60 + kq;
        } else {
            kq = ":0" + sum % 60 + kq;
        }
        sum /= 60;
        if (sum > 9) {
            kq = sum + kq;
        } else {
            kq = "0" + sum + kq;
        }
        return kq;
    }
    public void checkValue(int v){
        
            if (aa[I][J] == 0) {
                cells[I][J].setText(v + "");

                if (v == A[I][J]) {
                    a[I][J] = v;
                    cells[I][J].setForeground(Color.BLUE);

                    boolean check = true;
                    for (int i = 0; i < 9; i++) {
                        for (int j = 0; j < 9; j++) {
                            if (a[i][j] != A[i][j]) {
                                check = false;
                            }
                        }
                    }
                    if (check) {
                        lblNotice.setText("Bạn đã chiến thắng!");
                        JOptionPane.showMessageDialog(this, "Bạn đã chiến thắng!");
                        new SodokuFrame().timer.start();
                        this.newGame(cboxLevel.getSelectedIndex());
                        this.dispose();
                    }
                } else {
                    NO--;
                    a[I][J] = -1;
                    cells[I][J].setForeground(Color.red);
                    lblNotice.setText("Bạn còn " + NO + " lượt chơi.");
                    if (NO == -1) {
                        JOptionPane.showMessageDialog(null, "Bạn đã sai 3 lần.\n Hãy cố gắng hơn nhé!");
                        lblNotice.setText("Bạn đã thua!");
                        JOptionPane.showMessageDialog(this, "Bạn đã thua!");
                        this.newGame(cboxLevel.getSelectedIndex());
                        this.dispose();
                    }

                }
           
        }
    }

    public void newGame(int k) {
        
        this.clear();
        btnNewGame.setBackground(new Color(164, 211, 238));
        this.LV = k;
        lblTime.setText("00:00:00:00");
        lblNotice.setText("Ban con " + NO + " luot choi.");
        try {
            creatMatrix();
        } catch (FileNotFoundException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    public void checkSolution() {
        for (int i = 0; i < 9; i++) {
            for (int j = 0; j < 9; j++) {
                if (a[i][j] != A[i][j]) {
                    cells[i][j].setForeground(Color.BLUE);
                    cells[i][j].setText(A[i][j] + "");
                }
            }
        }
        timer.stop();
    }

    @Override
    public void dispose() {
        timer.stop();
        super.dispose();
    }

    /**
     * Creates new form SodokuFrame
     */
    public SodokuFrame() {
        initComponents();
        this.setLocationRelativeTo(null);
        this.setResizable(false);
        this.setVisible(true);
        generateBoard();
        try {
            creatMatrix();
        } catch (FileNotFoundException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        timer = new Timer(10, new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                lblTime.setText(next(lblTime));
            }
        });
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jOptionPane1 = new javax.swing.JOptionPane();
        jMenu1 = new javax.swing.JMenu();
        jMenu2 = new javax.swing.JMenu();
        jPopupMenu1 = new javax.swing.JPopupMenu();
        jOptionPane2 = new javax.swing.JOptionPane();
        jFileChooser1 = new javax.swing.JFileChooser();
        jOptionPane3 = new javax.swing.JOptionPane();
        jDialog1 = new javax.swing.JDialog();
        canvasstop = new java.awt.Canvas();
        jOptionPane4 = new javax.swing.JOptionPane();
        jDialog2 = new javax.swing.JDialog();
        pnlGameInfo = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        lblTime = new javax.swing.JTextField();
        lblNotice = new javax.swing.JTextField();
        cboxLevel = new javax.swing.JComboBox<>();
        stop = new javax.swing.JButton();
        pnlBoard = new javax.swing.JPanel();
        jPanel1 = new javax.swing.JPanel();
        btnNewGame = new javax.swing.JButton();
        btnCheckSolution = new javax.swing.JButton();
        hide = new javax.swing.JButton();
        note = new javax.swing.JButton();
        pnlBanphim = new javax.swing.JPanel();
        j2 = new javax.swing.JButton();
        j1 = new javax.swing.JButton();
        j3 = new javax.swing.JButton();
        j4 = new javax.swing.JButton();
        j7 = new javax.swing.JButton();
        j5 = new javax.swing.JButton();
        j6 = new javax.swing.JButton();
        j8 = new javax.swing.JButton();
        j9 = new javax.swing.JButton();
        reset = new javax.swing.JButton();
        clear = new javax.swing.JButton();

        jMenu1.setText("jMenu1");

        jMenu2.setText("jMenu2");

        jOptionPane3.setMaximumSize(new java.awt.Dimension(1000, 1000));
        jOptionPane3.setMinimumSize(new java.awt.Dimension(300, 300));
        jOptionPane3.setName(""); // NOI18N

        javax.swing.GroupLayout jDialog1Layout = new javax.swing.GroupLayout(jDialog1.getContentPane());
        jDialog1.getContentPane().setLayout(jDialog1Layout);
        jDialog1Layout.setHorizontalGroup(
            jDialog1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 400, Short.MAX_VALUE)
        );
        jDialog1Layout.setVerticalGroup(
            jDialog1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 300, Short.MAX_VALUE)
        );

        javax.swing.GroupLayout jDialog2Layout = new javax.swing.GroupLayout(jDialog2.getContentPane());
        jDialog2.getContentPane().setLayout(jDialog2Layout);
        jDialog2Layout.setHorizontalGroup(
            jDialog2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 400, Short.MAX_VALUE)
        );
        jDialog2Layout.setVerticalGroup(
            jDialog2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 300, Short.MAX_VALUE)
        );

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setBackground(new java.awt.Color(255, 255, 255));

        pnlGameInfo.setBorder(javax.swing.BorderFactory.createTitledBorder("Sodoku Game"));

        jLabel1.setText("Time:");

        lblTime.setText("00:00:00:00");
        lblTime.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                lblTimeActionPerformed(evt);
            }
        });

        lblNotice.setText("Ban con 3 luot choi.");

        cboxLevel.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "De", "Trung Binh", "Kho" }));
        cboxLevel.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cboxLevelActionPerformed(evt);
            }
        });

        stop.setText("Stop");
        stop.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                stopActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout pnlGameInfoLayout = new javax.swing.GroupLayout(pnlGameInfo);
        pnlGameInfo.setLayout(pnlGameInfoLayout);
        pnlGameInfoLayout.setHorizontalGroup(
            pnlGameInfoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, pnlGameInfoLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(cboxLevel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(stop)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(jLabel1)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(pnlGameInfoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(lblNotice, javax.swing.GroupLayout.DEFAULT_SIZE, 282, Short.MAX_VALUE)
                    .addComponent(lblTime))
                .addContainerGap())
        );
        pnlGameInfoLayout.setVerticalGroup(
            pnlGameInfoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, pnlGameInfoLayout.createSequentialGroup()
                .addGap(0, 0, Short.MAX_VALUE)
                .addGroup(pnlGameInfoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel1)
                    .addComponent(lblTime, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(lblNotice, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(15, 15, 15))
            .addGroup(pnlGameInfoLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(pnlGameInfoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(cboxLevel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(stop))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        pnlBoard.setBackground(new java.awt.Color(255, 255, 255));
        pnlBoard.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.RAISED));
        pnlBoard.setPreferredSize(new java.awt.Dimension(450, 450));
        pnlBoard.setLayout(new java.awt.GridLayout(1, 0));

        jPanel1.setRequestFocusEnabled(false);
        jPanel1.setVerifyInputWhenFocusTarget(false);

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 466, Short.MAX_VALUE)
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 446, Short.MAX_VALUE)
        );

        pnlBoard.add(jPanel1);
        jPanel1.getAccessibleContext().setAccessibleDescription("");

        btnNewGame.setText("New Game");
        btnNewGame.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnNewGameActionPerformed(evt);
            }
        });

        btnCheckSolution.setText("Check Solution ");
        btnCheckSolution.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnCheckSolutionActionPerformed(evt);
            }
        });

        hide.setText("Hide");
        hide.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                hideActionPerformed(evt);
            }
        });

        note.setText("Note");
        note.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                noteActionPerformed(evt);
            }
        });

        j2.setText("2");
        j2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                j2ActionPerformed(evt);
            }
        });

        j1.setText("1");
        j1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                j1ActionPerformed(evt);
            }
        });

        j3.setText("3");
        j3.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                j3ActionPerformed(evt);
            }
        });

        j4.setText("4");
        j4.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                j4ActionPerformed(evt);
            }
        });

        j7.setText("7");
        j7.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                j7ActionPerformed(evt);
            }
        });

        j5.setText("5");
        j5.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                j5ActionPerformed(evt);
            }
        });

        j6.setText("6");
        j6.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                j6ActionPerformed(evt);
            }
        });

        j8.setText("8");
        j8.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                j8ActionPerformed(evt);
            }
        });

        j9.setText("9");
        j9.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                j9ActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout pnlBanphimLayout = new javax.swing.GroupLayout(pnlBanphim);
        pnlBanphim.setLayout(pnlBanphimLayout);
        pnlBanphimLayout.setHorizontalGroup(
            pnlBanphimLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, pnlBanphimLayout.createSequentialGroup()
                .addGap(23, 23, 23)
                .addGroup(pnlBanphimLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addGroup(pnlBanphimLayout.createSequentialGroup()
                        .addComponent(j7, javax.swing.GroupLayout.PREFERRED_SIZE, 69, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addComponent(j8, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                    .addGroup(pnlBanphimLayout.createSequentialGroup()
                        .addComponent(j1, javax.swing.GroupLayout.PREFERRED_SIZE, 69, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addComponent(j2, javax.swing.GroupLayout.PREFERRED_SIZE, 69, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(pnlBanphimLayout.createSequentialGroup()
                        .addComponent(j4, javax.swing.GroupLayout.PREFERRED_SIZE, 69, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(19, 19, 19)
                        .addComponent(j5, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
                .addGap(18, 18, 18)
                .addGroup(pnlBanphimLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(j3, javax.swing.GroupLayout.PREFERRED_SIZE, 69, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(j6, javax.swing.GroupLayout.PREFERRED_SIZE, 69, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(j9, javax.swing.GroupLayout.PREFERRED_SIZE, 69, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        pnlBanphimLayout.setVerticalGroup(
            pnlBanphimLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pnlBanphimLayout.createSequentialGroup()
                .addGap(24, 24, 24)
                .addGroup(pnlBanphimLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(j2, javax.swing.GroupLayout.PREFERRED_SIZE, 56, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(j1, javax.swing.GroupLayout.PREFERRED_SIZE, 56, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(j3, javax.swing.GroupLayout.PREFERRED_SIZE, 56, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(pnlBanphimLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(j6, javax.swing.GroupLayout.PREFERRED_SIZE, 54, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(j5, javax.swing.GroupLayout.PREFERRED_SIZE, 55, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(j4, javax.swing.GroupLayout.PREFERRED_SIZE, 55, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(pnlBanphimLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, pnlBanphimLayout.createSequentialGroup()
                        .addGap(0, 0, Short.MAX_VALUE)
                        .addComponent(j8, javax.swing.GroupLayout.PREFERRED_SIZE, 55, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(j7, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(j9, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap())
        );

        reset.setText("Reset");
        reset.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                resetActionPerformed(evt);
            }
        });

        clear.setText("Clear");
        clear.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                clearActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(pnlGameInfo, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(pnlBoard, javax.swing.GroupLayout.PREFERRED_SIZE, 470, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addGap(19, 19, 19)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(layout.createSequentialGroup()
                                .addComponent(pnlBanphim, javax.swing.GroupLayout.PREFERRED_SIZE, 268, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addContainerGap(43, Short.MAX_VALUE))
                            .addGroup(layout.createSequentialGroup()
                                .addComponent(reset)
                                .addGap(18, 18, 18)
                                .addComponent(clear)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addComponent(note)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addComponent(hide)
                                .addGap(22, 22, 22))))
                    .addGroup(layout.createSequentialGroup()
                        .addGap(13, 13, 13)
                        .addComponent(btnNewGame, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addContainerGap())
                    .addGroup(layout.createSequentialGroup()
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(btnCheckSolution, javax.swing.GroupLayout.PREFERRED_SIZE, 170, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(77, 77, 77))))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addComponent(pnlGameInfo, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addGap(21, 21, 21)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(note)
                            .addComponent(hide)
                            .addComponent(reset)
                            .addComponent(clear))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(pnlBanphim, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(btnCheckSolution)
                        .addGap(49, 49, 49)
                        .addComponent(btnNewGame, javax.swing.GroupLayout.PREFERRED_SIZE, 94, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(0, 0, Short.MAX_VALUE))
                    .addGroup(layout.createSequentialGroup()
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(pnlBoard, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
                .addGap(13, 13, 13))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void btnCheckSolutionActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnCheckSolutionActionPerformed
        this.checkSolution();
    }//GEN-LAST:event_btnCheckSolutionActionPerformed

    private void btnNewGameActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnNewGameActionPerformed
        // TODO add your handling code here:
        this.newGame(cboxLevel.getSelectedIndex());
    }//GEN-LAST:event_btnNewGameActionPerformed

    private void lblTimeActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_lblTimeActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_lblTimeActionPerformed

    private void cboxLevelActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cboxLevelActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_cboxLevelActionPerformed

    private void j4ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_j4ActionPerformed
        // TODO add your handling code here:
        selectNumber(4);
        //selectNumber2(4);
        setValueInInput(I, J);
        checkValue(4);
    }//GEN-LAST:event_j4ActionPerformed

    private void j1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_j1ActionPerformed
        selectNumber(1);
       // selectedNumber2(1);
        setValueInInput(I, J);
        checkValue(1);
        // TODO add your handling code here:


    }//GEN-LAST:event_j1ActionPerformed

    private void j2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_j2ActionPerformed
        // TODO add your handling code here:
        selectNumber(2);
       // selectNumber2(2);
        setValueInInput(I, J);
        checkValue(2);
    }//GEN-LAST:event_j2ActionPerformed

    private void j3ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_j3ActionPerformed
        // TODO add your handling code here:
        selectNumber(3);
      //  selectNumber2(3);
        setValueInInput(I, J);
        checkValue(3);
    }//GEN-LAST:event_j3ActionPerformed

    private void j5ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_j5ActionPerformed
        // TODO add your handling code here:
        selectNumber(5);
       // selectNumber2(5);
        setValueInInput(I, J);
        checkValue(5);
    }//GEN-LAST:event_j5ActionPerformed

    private void j6ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_j6ActionPerformed
        // TODO add your handling code here:
        selectNumber(6);
       // selectNumber2(6);
        setValueInInput(I, J);
    }//GEN-LAST:event_j6ActionPerformed

    private void j7ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_j7ActionPerformed
        // TODO add your handling code here:
        selectNumber(7);
      //  selectNumber2(7);
        setValueInInput(I, J);
        checkValue(7);
    }//GEN-LAST:event_j7ActionPerformed

    private void j8ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_j8ActionPerformed
        // TODO add your handling code here:
        selectNumber(8);
      //  selectNumber2(8);
        setValueInInput(I, J);
        checkValue(8);
    }//GEN-LAST:event_j8ActionPerformed

    private void j9ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_j9ActionPerformed
        // TODO add your handling code here:
        selectNumber(9);
       // selectNumber2(9);
        setValueInInput(I, J);
        checkValue(9);
    }//GEN-LAST:event_j9ActionPerformed


    private void resetActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_resetActionPerformed
        // TODO add your handling code here:

        // TODO add your handling code here:
        for (int i = 0; i < 9; i++) {
            for (int j = 0; j < 9; j++) {
                if (a[i][j] != aa[i][j]) {
                    cells[i][j].setText("");

                    cells[i][j].setBackground(Color.white);
                }
            }

        }
    }//GEN-LAST:event_resetActionPerformed
    private JFrame jOptionPane9;
    int count = 0;
    private void stopActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_stopActionPerformed
        // TODO add your handling code here:
        timer.stop();
        int reply = JOptionPane.showConfirmDialog(null, "Are you sure you want to close?", "Star?", JOptionPane.YES_NO_OPTION);
        if (reply == JOptionPane.YES_OPTION) {
            timer.start();
        } else if(reply == JOptionPane.NO_OPTION){
            menuJFrame = new MenuJFrame();
            menuJFrame.setVisible(true);
            dispose();
        }
        

        

    }//GEN-LAST:event_stopActionPerformed
//    JButton[] numberInputs2 = new JButton[9]; // To take user inputs which are 1 to 9 numbers
//    int curNum2 = 1;
//
//    public void selectNumber2(int num) {
//        int prevNum = curNum2;
//        JButton prevSelected = numberInputs2[prevNum - 1];
//        prevSelected.setBackground(new Color(255, 255, 255));
//        JButton curSelected = numberInputs2[num - 1];
//        curSelected.setBackground(new Color(164, 211, 238));
//        curNum2 = num;
//        
//    }
//    public void setValueInInput2(int i, int j) {
//        if (a[i][j] == 0) {
//            cells[i][j].setText(Integer.toString(curNum2));
//            cells[i][j].setForeground(Color.gray);
//            cells[i][j].setBackground(Color.white);
//        }
//    }
//
//    
    private void noteActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_noteActionPerformed
        // TODO add your handling code here:
        setValueInInput2(I, J);
        
    }//GEN-LAST:event_noteActionPerformed

    private void hideActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_hideActionPerformed
        // TODO add your handling code here:
        for (int i = 0; i < 9; i++) {
            for (int j = 0; j < 9; j++) {
                if (a[i][j] != aa[i][j]) {
                    cells[i][j].setText(A[i][j] + "");
                    cells[i][j].setForeground(Color.BLUE);
                    cells[i][j].setBackground(Color.white);
                }
            }

        }

    }//GEN-LAST:event_hideActionPerformed

    private void clearActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_clearActionPerformed
        // TODO add your handling code here:
        cells[I][J].setText("");
        cells[I][J].setBackground(Color.white);
    }//GEN-LAST:event_clearActionPerformed

    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        /* Set the Nimbus look and feel */
        //<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
        /* If Nimbus (introduced in Java SE 6) is not available, stay with the default look and feel.
         * For details see http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html 
         */
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ClassNotFoundException ex) {
            java.util.logging.Logger.getLogger(SodokuFrame.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(SodokuFrame.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(SodokuFrame.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(SodokuFrame.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new SodokuFrame().timer.start();
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnCheckSolution;
    private javax.swing.JButton btnNewGame;
    private java.awt.Canvas canvasstop;
    private javax.swing.JComboBox<String> cboxLevel;
    private javax.swing.JButton clear;
    private javax.swing.JButton hide;
    private javax.swing.JButton j1;
    private javax.swing.JButton j2;
    private javax.swing.JButton j3;
    private javax.swing.JButton j4;
    private javax.swing.JButton j5;
    private javax.swing.JButton j6;
    private javax.swing.JButton j7;
    private javax.swing.JButton j8;
    private javax.swing.JButton j9;
    private javax.swing.JDialog jDialog1;
    private javax.swing.JDialog jDialog2;
    private javax.swing.JFileChooser jFileChooser1;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JMenu jMenu1;
    private javax.swing.JMenu jMenu2;
    private javax.swing.JOptionPane jOptionPane1;
    private javax.swing.JOptionPane jOptionPane2;
    private javax.swing.JOptionPane jOptionPane3;
    private javax.swing.JOptionPane jOptionPane4;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPopupMenu jPopupMenu1;
    private javax.swing.JTextField lblNotice;
    private javax.swing.JTextField lblTime;
    private javax.swing.JButton note;
    private javax.swing.JPanel pnlBanphim;
    private javax.swing.JPanel pnlBoard;
    private javax.swing.JPanel pnlGameInfo;
    private javax.swing.JButton reset;
    private javax.swing.JButton stop;
    // End of variables declaration//GEN-END:variables

    public void insertNumber(Cell textField, int value) {
        history.push(textField);
        textField.setText(Integer.toString(value));
    }

    private Stack<Cell> history = new Stack<>();

    @Override

    public void actionPerformed(ActionEvent e) {

        for (int i = 0; i < 9; i++) {

            for (int j = 0; j < 9; j++) {

                cells[i][j].setBackground(Color.white);

            }
        }

        String s = e.getActionCommand();
        int k = s.indexOf(32);
        int i = Integer.parseInt(s.substring(0, k));
        int j = Integer.parseInt(s.substring(k + 1, s.length()));
        I = i;
        J = j;

        if (a[I][J] > 0) {
            for (i = 0; i < 9; i++) {
                for (j = 0; j < 9; j++) {
                    if (a[i][j] == a[I][J]) {
                        cells[I][J].setBackground(new Color(96, 123, 139));
                        cells[i][j].setBackground(new Color(108, 166, 205));

                    }

                    cells[I][j].setBackground(new Color(164, 211, 238));
                    cells[i][J].setBackground(new Color(164, 211, 238));

                }
            }
        }

    }

    @Override
    public void keyTyped(KeyEvent e) {
        
    }

    @Override
    public void keyPressed(KeyEvent e) {

        int v = e.getKeyCode();


        if ((v >= 49 && v <= 57) || (v >= 97 && v <= 105)) {

            if (v >= 49 && v <= 57) {
                v -= 48;
            }
            if (v >= 97 && v <= 105) {
                v -= 96;
            }
            if (aa[I][J] == 0) {
                cells[I][J].setText(v + "");

                if (v == A[I][J]) {
                    a[I][J] = v;
                    cells[I][J].setForeground(Color.BLUE);

                    boolean check = true;
                    for (int i = 0; i < 9; i++) {
                        for (int j = 0; j < 9; j++) {
                            if (a[i][j] != A[i][j]) {
                                check = false;
                            }
                        }
                    }
                    if (check) {
                        lblNotice.setText("Bạn đã chiến thắng!");
                        JOptionPane.showMessageDialog(this, "Bạn đã chiến thắng!");
                        new SodokuFrame().timer.start();
                        this.newGame(cboxLevel.getSelectedIndex());
                        this.dispose();
                    }
                } else {
                    NO--;
                    a[I][J] = -1;
                    cells[I][J].setForeground(Color.red);
                    lblNotice.setText("Bạn còn " + NO + " lượt chơi.");
                    if (NO == -1) {
                        JOptionPane.showMessageDialog(null, "Bạn đã sai 3 lần.\n Hãy cố gắng hơn nhé!");
                        lblNotice.setText("Bạn đã thua!");
                        JOptionPane.showMessageDialog(this, "Bạn đã thua!");
                        this.newGame(cboxLevel.getSelectedIndex());
                        this.dispose();
                    }

                }
            }
        }
    }

    @Override
    public void keyReleased(KeyEvent e) {
    }

    @Override
    public void mouseReleased(MouseEvent e) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void mouseEntered(MouseEvent e) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void mouseExited(MouseEvent e) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void mousePressed(MouseEvent e) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void mouseClicked(MouseEvent e) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
}
