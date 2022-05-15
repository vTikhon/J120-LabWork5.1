package ru.avalon.vergentev.j120.labwork5a;
import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.event.*;
import javax.swing.tree.*;
import java.io.*;
import java.nio.charset.StandardCharsets;

public class TxtViewer extends JFrame implements KeyListener {
    private static final JScrollPane panelForTree = new JScrollPane();
    private static final JScrollPane panelForText = new JScrollPane();
    private static final JSplitPane splitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, panelForTree, panelForText);

    private static final File file = new File(System.getProperty("user.dir"));
    private static final DefaultMutableTreeNode nodeStart = new DefaultMutableTreeNode(System.getProperty("user.dir"));
    private TreePath path;
    private JTree tree;

    private StringBuilder text;
    private static final JTextArea textArea = new JTextArea("");

    public TxtViewer() {
        initializationFrame();
        initializationTree();
        initializationTextArea();
    }

    public void initializationFrame () {
        setTitle("Txt viewer");
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setSize(1200, 900);
        setResizable(true);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());
        add(splitPane);
    }

    public void getDirectoriesForTree (DefaultMutableTreeNode node, File file) {
        if (file.getPath().equals(System.getProperty("user.dir"))) {
            if (file.isDirectory()) {
                File[] listFiles = file.listFiles();
                assert listFiles != null;
                for (File eachFile : listFiles) getDirectoriesForTree(node, eachFile);
            }
        } else {
            DefaultMutableTreeNode nodeChild = new DefaultMutableTreeNode(file);
            node.add(nodeChild);
            if (file.isDirectory()) {
                File[] listFiles = file.listFiles();
                assert listFiles != null;
                for (File eachFile : listFiles) getDirectoriesForTree(nodeChild, eachFile);
            }
        }
    }

    public void initializationTree () {
        getDirectoriesForTree(nodeStart, file);
        tree = new JTree(nodeStart);
        panelForTree.setViewportView(tree);
        tree.setRootVisible(true);
        tree.setBackground(Color.WHITE);
        tree.addTreeSelectionListener(e -> valueChanged(e));
        tree.addKeyListener(this);
    }

    public void initializationTextArea () {
        panelForText.setViewportView(textArea);
        textArea.setBackground(Color.ORANGE);
    }

    public void valueChanged(TreeSelectionEvent e) {
        DefaultMutableTreeNode node = (DefaultMutableTreeNode) e.getPath().getLastPathComponent();
        setTitle(String.valueOf(node));
        textArea.setText("");
        if (new File(String.valueOf(node)).getName().endsWith("txt")) {
            reader(new File(String.valueOf(node)));
            textArea.setText(String.valueOf(text));
        }
        //часть необходимая в сочетании с клавишей ENTER (запоминаем директорию если выбрана папка)
        if (!((DefaultMutableTreeNode) e.getPath().getLastPathComponent()).isLeaf()) {
            path = e.getPath();
        }
    }

    public StringBuilder reader (File file) {
        if (!file.canRead()) textArea.setText("File can't be readable !");
        int symbolExisting;
        try {
            FileReader fileReader = new FileReader(file, StandardCharsets.UTF_8);
            text = new StringBuilder();
            while ((symbolExisting = fileReader.read()) != -1) {
                text.append((char)symbolExisting);
            }
            fileReader.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return text;
    }

    @Override
    public void keyTyped(KeyEvent e) {}
    @Override
    public void keyPressed(KeyEvent e) {
        if (e.getKeyCode() == KeyEvent.VK_ENTER) {
            tree.expandPath(path);
        }
    }
    @Override
    public void keyReleased(KeyEvent e) {}
}
