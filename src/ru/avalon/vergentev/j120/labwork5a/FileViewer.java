package ru.avalon.vergentev.j120.labwork5a;
import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.event.*;
import javax.swing.tree.*;
import java.io.*;
import java.nio.charset.StandardCharsets;

public class FileViewer extends JFrame implements TreeSelectionListener, KeyListener {
    File file = new File(System.getProperty("user.dir"));
    DefaultMutableTreeNode root = new DefaultMutableTreeNode();
    StringBuilder text;
    JScrollPane panelForTree = new JScrollPane();
    JScrollPane panelForText = new JScrollPane();
    JTree tree;
    JTextArea textArea = new JTextArea("");

    public FileViewer() {
        initializationFrame();
        initializationTree(file);
        initializationTextArea();
    }

    public void initializationFrame () {
        setTitle("File viewer");
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setSize(1200, 900);
        setResizable(true);
        setLocationRelativeTo(null);
        setLayout(new GridLayout(1 , 2));
    }

    public void initializationTree (File file) {
        getDirectoriesForTree(root, file);
        add(panelForTree);
        tree = new JTree(root);
        panelForTree.setViewportView(tree);
        tree.setRootVisible(true);
        tree.setBackground(Color.LIGHT_GRAY);
        tree.addTreeSelectionListener(this);
        tree.addKeyListener(this);
    }

    public void getDirectoriesForTree (DefaultMutableTreeNode node, File file) {
        DefaultMutableTreeNode child = new DefaultMutableTreeNode(file);
        node.add(child);
        if (file.isDirectory()) {
            File[] listFiles = file.listFiles();
            assert listFiles != null;
            for (File value : listFiles) getDirectoriesForTree(child, value);
        }
    }

    public void initializationTextArea () {
        add(panelForText);
        panelForText.setViewportView(textArea);
        textArea.setBackground(Color.ORANGE);
    }

    @Override
    public void valueChanged(TreeSelectionEvent e) {
        DefaultMutableTreeNode node = (DefaultMutableTreeNode) e.getPath().getLastPathComponent();
        setTitle(String.valueOf(node));
        textArea.setText("");
        if (new File(String.valueOf(node)).getName().endsWith("txt")) {
            reader(new File(String.valueOf(node)));
            textArea.setText(String.valueOf(text));
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
            System.out.println("aaaa");
        }
    }
    @Override
    public void keyReleased(KeyEvent e) {}
}
