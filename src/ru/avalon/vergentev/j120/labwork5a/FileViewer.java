package ru.avalon.vergentev.j120.labwork5a;
import java.awt.*;
import java.io.*;
import java.nio.charset.StandardCharsets;
import javax.swing.*;
import javax.swing.tree.*;

public class FileViewer extends JFrame {
    File file = new File("C:\\Users\\Tikhon\\Documents\\JavaProjects\\J120-LabWork5.1");
    DefaultMutableTreeNode root = new DefaultMutableTreeNode();
    StringBuilder data;
    JScrollPane panelForTree = new JScrollPane();
    JScrollPane panelForText = new JScrollPane();
    JTree tree;
    JTextArea textArea = new JTextArea(String.valueOf(data));

    public FileViewer() {
        setTitle("File viewer");
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setSize(900, 600);
        setResizable(true);
        setLocationRelativeTo(null);
        setLayout(new GridLayout(1 , 2));

        getDirectoriesForTree(root, file);

        add(panelForTree);
        tree = new JTree(root);
        panelForTree.setViewportView(tree);
        tree.setRootVisible(true);
        tree.setBackground(Color.LIGHT_GRAY);

        add(panelForText);
        panelForText.setViewportView(textArea);
        textArea.setBackground(Color.ORANGE);
    }

    public void getDirectoriesForTree (DefaultMutableTreeNode node, File file) {
        if (!file.isDirectory()) {
            file.getName();
            DefaultMutableTreeNode child = new DefaultMutableTreeNode(file);
            node.add(child);
        } else {
            DefaultMutableTreeNode child = new DefaultMutableTreeNode(file);
            node.add(child);
            File fileList[] = file.listFiles();
            for (int i = 0; i  < fileList.length; i++)
                getDirectoriesForTree(child, fileList[i]);
        }
    }

    public StringBuilder reader (File file) {
        if (!file.exists() || !file.canRead()) throw new SecurityException("File can't be readable or doesn't exist !!!");
        int symbolExisting;
        try {
            FileReader fileReader = new FileReader(file, StandardCharsets.UTF_8);
            data = new StringBuilder();
            while ((symbolExisting = fileReader.read()) != -1) {
                data.append((char)symbolExisting);
            }
            fileReader.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return data;
    }


}
