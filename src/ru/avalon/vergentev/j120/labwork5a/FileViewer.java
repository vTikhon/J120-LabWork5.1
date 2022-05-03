package ru.avalon.vergentev.j120.labwork5a;
import java.awt.*;
import java.io.*;
import java.nio.charset.StandardCharsets;
import javax.swing.*;
import javax.swing.event.TreeSelectionEvent;
import javax.swing.event.TreeSelectionListener;
import javax.swing.tree.*;

public class FileViewer extends JFrame implements TreeSelectionListener {
    File file = new File(System.getProperty("user.dir"));
    DefaultMutableTreeNode root = new DefaultMutableTreeNode();
    StringBuilder text;
    JScrollPane panelForTree = new JScrollPane();
    JScrollPane panelForText = new JScrollPane();
    JTree tree;
    JTextArea textArea = new JTextArea(String.valueOf(text));

    public FileViewer() {
        setTitle("File viewer");
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setSize(1200, 900);
        setResizable(true);
        setLocationRelativeTo(null);
        setLayout(new GridLayout(1 , 2));

        getDirectoriesForTree(root, file);

        add(panelForTree);
        tree = new JTree(root);
        panelForTree.setViewportView(tree);
        tree.setRootVisible(true);
        tree.setBackground(Color.LIGHT_GRAY);
        tree.addTreeSelectionListener(this);

        add(panelForText);
        panelForText.setViewportView(textArea);
        textArea.setBackground(Color.ORANGE);
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
}
