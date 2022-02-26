package com.fakerandroid.as.editor;

import javax.swing.*;
import javax.swing.tree.DefaultTreeModel;

import com.intellij.ui.LoadingNode;
import com.intellij.ui.treeStructure.Tree;
public class DatViewPanel {
    private JPanel jpanel;
    private Tree myTree;
    private DefaultTreeModel myTreeModel;
    private void createUIComponents() {
        // TODO: place custom component creation code here
        myTreeModel = new DefaultTreeModel(new LoadingNode());
        myTree = new Tree(myTreeModel);
    }

    public JComponent getContainer() {
        return jpanel;
    }
    public JComponent getPreferredFocusedComponent() {
        return myTree;
    }
}
