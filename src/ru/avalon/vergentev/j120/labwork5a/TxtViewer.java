package ru.avalon.vergentev.j120.labwork5a;
import javax.swing.*;
import javax.swing.event.*;
import javax.swing.tree.*;
import java.awt.*;
import java.awt.event.*;
import java.io.*;
import java.nio.charset.StandardCharsets;

public class TxtViewer extends JFrame implements KeyListener {
    private static final JScrollPane panelForTree = new JScrollPane();
    private static final JScrollPane panelForText = new JScrollPane();
    private static final JSplitPane splitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, panelForTree, panelForText);

    private DefaultTreeModel defaultTreeModel;
    private File file;
    private DefaultMutableTreeNode nodeStart;
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

    public MutableTreeNode getDirectoriesForTree (DefaultMutableTreeNode node, File file) {
        DefaultMutableTreeNode nodeChild = new DefaultMutableTreeNode(file.getName());
        node.add(nodeChild);
        if (file.isDirectory() && file.exists()) {
            File[] listFiles = file.listFiles();
            if (listFiles != null) {
                for (File eachFile : listFiles) getDirectoriesForTree(nodeChild, eachFile);
            }
        }
        return node;
    }

    public void initializationTree () {
        nodeStart = new DefaultMutableTreeNode("<<..>>");
        file = new File(System.getProperty("user.dir"));
        defaultTreeModel = new DefaultTreeModel(getDirectoriesForTree(nodeStart, file));
        tree = new JTree(defaultTreeModel);  //new DefaultTreeModel(nodeStart)
        addTree();
    }

    public void addTree () {
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
        //часть для перестроения <<..>>
        if (e.getPath().getLastPathComponent() == nodeStart && file.getParentFile() != null) {
            nodeStart.removeAllChildren();
            nodeStart = new DefaultMutableTreeNode("<<..>>");
            file = new File(file.getAbsoluteFile().getParent());
            defaultTreeModel.reload();
            defaultTreeModel = new DefaultTreeModel(getDirectoriesForTree(nodeStart, file));
            tree = new JTree(defaultTreeModel);
            addTree();
        }
        //чтение txt файлов
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
    public void algorithmIfEnterIsPushed () {
        if (tree.isCollapsed(path)) {
            tree.expandPath(path);
        } else {
            tree.collapsePath(path);
        }
    }

    @Override
    public void keyTyped(KeyEvent e) {}
    @Override
    public void keyPressed(KeyEvent e) {
        if (e.getKeyCode() == KeyEvent.VK_ENTER) {algorithmIfEnterIsPushed();}
    }
    @Override
    public void keyReleased(KeyEvent e) {}
}
