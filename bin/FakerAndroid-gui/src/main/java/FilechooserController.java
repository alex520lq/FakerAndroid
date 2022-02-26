import com.luhuiguo.chinese.ChineseUtils;
import com.luhuiguo.chinese.pinyin.PinyinFormat;
import com.fakerandroid.decoder.api.Transfer;
import com.fakerandroid.decoder.pipeline.TransformInvocation;
import com.fakerandroid.decoder.util.TextUtil;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.input.DragEvent;
import javafx.scene.input.Dragboard;
import javafx.scene.input.MouseEvent;
import javafx.scene.input.TransferMode;
import javafx.scene.layout.VBox;
import javafx.stage.*;

import java.io.*;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class FilechooserController {

    @FXML
    private Button btn01;

    @FXML
    private Button btn02;

    @FXML
    private Button btn03;

    @FXML
    private TextArea txa;

    @FXML
    private VBox vbox;

    @FXML
    private TextField apkPathTextField;

    @FXML
    private TextField projectPathTextField;

    Stage stage;
    File outProjectCache;
    File apkDirCache;
    @FXML
    void initialize() {
        assert btn01 != null : "fx:id=\"btn01\" was not injected: check your FXML file 'FileChooser.fxml'.";
        assert btn02 != null : "fx:id=\"btn02\" was not injected: check your FXML file 'FileChooser.fxml'.";
        assert btn03 != null : "fx:id=\"btn02\" was not injected: check your FXML file 'FileChooser.fxml'.";
        assert txa != null : "fx:id=\"txa\" was not injected: check your FXML file 'FileChooser.fxml'.";
        assert vbox != null : "fx:id=\"vbox\" was not injected: check your FXML file 'FileChooser.fxml'.";
        assert apkPathTextField != null : "fx:id=\"apkPathTextField\" was not injected: check your FXML file 'FileChooser.fxml'.";
        assert projectPathTextField != null : "fx:id=\"projectPathTextField\" was not injected: check your FXML file 'FileChooser.fxml'.";
        Platform.runLater(new Runnable() {
            @Override
            public void run() {
                stage = (Stage) btn01.getScene().getWindow();
            }
        });
        apkDirCache = new File(System.getProperty("java.io.tmpdir"),"apkDirCach.tmp");
        outProjectCache = new File(System.getProperty("java.io.tmpdir"),"outProjectCache.tmp");
    }
    File apkFile;
    @FXML
    void btn01OnAction(ActionEvent event) {
        FileChooser fch = new FileChooser();
        fch.setTitle("Select File !!");
//        fch.setInitialDirectory(new File(System.getProperty("user.home")));
        setApkInitialDirectory(fch,apkDirCache);

        fch.getExtensionFilters().addAll(
                new FileChooser.ExtensionFilter("Apk", "*.apk")
        );
        apkFile = fch.showOpenDialog(this.stage);
        if (apkFile != null) {
            this.apkPathTextField.clear();
            this.apkPathTextField.setText(apkFile.getPath());
            cacheApkInitialDirectory(apkDirCache,apkFile);
            File projectDir = new File(apkFile.getParent(), ChineseUtils.toPinyin(apkFile.getName().replace(".apk",""), PinyinFormat.TONELESS_PINYIN_FORMAT).replace(" ","-"));
            this.projectPathTextField.setText(projectDir.getAbsolutePath());
        }
    }
    ExecutorService executor = Executors.newCachedThreadPool();
    @FXML
    void btn03OnAction(ActionEvent event) {
        String apkPath = this.apkPathTextField.getText();
        String dirPath = this.projectPathTextField.getText();
        if(TextUtil.isEmpty(apkPath)||TextUtil.isEmpty(dirPath)){
            showPopupMessage("ApkPath or OutDir cant empty",stage);
            return;
        }

        File apkFile = new File(apkPath);
        if(!apkFile.exists()){
            showPopupMessage("Apk !exists !",stage);
            return;
        }
        if(!apkPath.endsWith(".apk")){
            showPopupMessage("invalid file !",stage);
            return;
        }
        File dirFile = new File(dirPath);
        if(!dirFile.getParentFile().exists()){
            showPopupMessage("Dir !exists !",stage);
            return;
        }
        TestTask testTask = new TestTask(txa,apkPath,dirPath,btn03);
        executor.execute(testTask);
    }
    class TestTask extends Thread {
        final TextArea textArea;String apkPath;String outPath;Button triggerButton;
        TestTask(TextArea textArea,String apkPath,String outPath,Button triggerButton) {
            this.textArea =textArea;
            this.apkPath =apkPath;
            this.outPath = outPath;
            this.triggerButton = triggerButton;
        }
        public void run() {
            triggerButton.setDisable(true);
            new Transfer(apkPath, outPath, new TransformInvocation() {
                @Override
                public void callBack(String msg) {
                    textArea.appendText(msg+"\r\n");
                }
            }).translate();
            triggerButton.setDisable(false);
        }
    }
    @FXML
    void btn02OnAction(ActionEvent event) {
        DirectoryChooser fch = new DirectoryChooser();
        fch.setTitle("Select File !!");
        if(apkFile==null||!apkFile.exists()){
            showPopupMessage("Please choose a apk first!",stage);
            return;
        }
        setOutProjectInitialDirectory(fch,outProjectCache);
        File file = fch.showDialog(this.stage);
        if (file != null) {
        if(apkFile!=null&&apkFile.exists()){
            File projectDir = new File(file, ChineseUtils.toPinyin(apkFile.getName().replace(".apk",""), PinyinFormat.TONELESS_PINYIN_FORMAT).replace(" ","-"));
            this.projectPathTextField.clear();
            this.projectPathTextField.setText(projectDir.getAbsolutePath());
        }
        cacheOutProjectInitialDirectory(outProjectCache,file);
        }
    }

    @FXML
    void onDragOverApk(DragEvent dragEvent) {
        if (dragEvent.getGestureSource() != dragEvent
                && dragEvent.getDragboard().hasFiles()) {
            dragEvent.acceptTransferModes(TransferMode.COPY_OR_MOVE);
        }
        dragEvent.consume();
    }

    @FXML
    void onDragDroppedApk(DragEvent dragEvent) {
        Dragboard db = dragEvent.getDragboard();
        boolean success = false;
        if (db.hasFiles()) {
            File apkFile = db.getFiles().iterator().next();
            this.apkPathTextField.setText(apkFile.getAbsolutePath());
            //TODO 自动赋值
            this.projectPathTextField.setText(apkFile.getParent());
            success = true;
        }
        dragEvent.setDropCompleted(success);
        dragEvent.consume();
    }

    private void setApkInitialDirectory(FileChooser fileChooser, File cacheFile) {
        if (cacheFile.exists()) {
            try (InputStream inputStream = new FileInputStream(cacheFile)) {
                byte[] bytes = new byte[(int) cacheFile.length()];
                inputStream.read(bytes);
                File directory = new File(new String(bytes));
                if (directory.exists()) {
                    fileChooser.setInitialDirectory(directory);
                }
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            } catch (IOException e1) {
                e1.printStackTrace();
            }
        } else {
            fileChooser.setInitialDirectory(new File(System.getProperty("user.home")));
        }
    }

    private void cacheApkInitialDirectory(File cacheFile,File file) {
        try (OutputStream outputStream = new FileOutputStream(cacheFile)) {
            byte[] bytes = file.getParent().getBytes();
            outputStream.write(bytes);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    private void setOutProjectInitialDirectory(DirectoryChooser directoryChooser,File cacheFile) {
        if (cacheFile.exists()) {
            try (InputStream inputStream = new FileInputStream(cacheFile)) {
                byte[] bytes = new byte[(int) cacheFile.length()];
                inputStream.read(bytes);
                File directory = new File(new String(bytes));
                if (directory.exists()) {
                    directoryChooser.setInitialDirectory(directory);
                }
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            } catch (IOException e1) {
                e1.printStackTrace();
            }
        } else {
            directoryChooser.setInitialDirectory(new File(System.getProperty("user.home")));
        }
    }
    public static void showPopupMessage(final String message, final Stage stage) {
        final Popup popup = createPopup(message);
        popup.setOnShown(new EventHandler<WindowEvent>() {
            @Override
            public void handle(WindowEvent e) {
                popup.setX(stage.getX() + stage.getWidth()/2 - popup.getWidth()/2);
                popup.setY(stage.getY() + stage.getHeight()/2 - popup.getHeight()/2);
            }
        });
        popup.show(stage);
    }
    public static Popup createPopup(final String message) {
        final Popup popup = new Popup();
        popup.setAutoFix(true);
        popup.setAutoHide(true);
        popup.setHideOnEscape(true);
        Label label = new Label(message);
        label.setOnMouseReleased(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent e) {
                popup.hide();
            }
        });
        label.getStyleClass().add("popup");
        popup.getContent().add(label);
        return popup;
    }
    private void cacheOutProjectInitialDirectory(File cacheFile,File file) {
        try (OutputStream outputStream = new FileOutputStream(cacheFile)) {
            byte[] bytes = file.getAbsolutePath().getBytes();
            outputStream.write(bytes);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
