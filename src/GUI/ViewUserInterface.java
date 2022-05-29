package GUI;

import History.ControllerBackup;
import Replace.ControllerReplace;
import Search.ControllerSearch;

import javax.swing.*;
import java.awt.*;
import java.io.File;
import java.io.IOException;
import java.net.URI;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Objects;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class ViewUserInterface extends JFrame {

    private JPanel panelMain;
    private JLabel labelRequiredAction;
    private JButton buttonDLSSWebsite;
    private JButton buttonAddGameDirectory;
    private JLabel labelRestoreBackup;
    private JButton buttonRestoreBackup;
    private JLabel labelGitHubLink;
    private JButton buttonReplaceDLSS;
    private JLabel labelProgress;
    private JLabel labelProgressUpdate;

    private ExecutorService controllerService;
    public static boolean isControllerRunning;

    public ViewUserInterface() {

        SwingUtilities.invokeLater(this::initUserInterface);
        SwingUtilities.invokeLater(this::initNonUserInterface);

        buttonDLSSWebsite.addActionListener(e -> {

            try {
                Desktop desktop = Desktop.getDesktop();
                desktop.browse(URI.create("https://www.techpowerup.com/download/nvidia-dlss-dll/"));
            } catch (IOException ex) { throw new RuntimeException(ex); }

        });

        buttonAddGameDirectory.addActionListener(e -> {

            if (isControllerRunning) return;

            File gameRootDir = null;

            JFileChooser dirChooser = new JFileChooser();
            dirChooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
            dirChooser.setDialogTitle("Select Game Root Directory");

            int dirChoice = dirChooser.showDialog(this, "Select");

            if (dirChoice == JFileChooser.APPROVE_OPTION) gameRootDir = dirChooser.getSelectedFile();

            if (gameRootDir != null) responseButtonAddGameDirectory(gameRootDir);

        });

        buttonReplaceDLSS.addActionListener(e -> {

            if (isControllerRunning) return;

            File[] DLSSDir = new File("DLSS").listFiles();

            if (DLSSDir == null || DLSSDir.length == 0) {

                showMessage("DLSS directory is empty!");
                return;

            }

            ControllerBackup controllerBackup = new ControllerBackup(this);
            ControllerReplace controllerReplace = new ControllerReplace(this, false);
            this.controllerService.submit(controllerBackup);
            this.controllerService.submit(controllerReplace);

        });

        buttonRestoreBackup.addActionListener(e -> {

            if (isControllerRunning) return;

            File[] backupDir = new File("Backup").listFiles();

            if (backupDir == null || backupDir.length == 0) {

                showMessage("Backup directory is empty!");
                return;

            }

            ControllerReplace controllerReplace = new ControllerReplace(this, true);
            this.controllerService.submit(controllerReplace);

        });

    }

    private void responseButtonAddGameDirectory(File gameRootDir) {

        ControllerSearch controllerSearch = new ControllerSearch(this, gameRootDir);
        this.controllerService.submit(controllerSearch);

    }

    private void showMessage(String text) {

        SwingUtilities.invokeLater(() ->
                JOptionPane.showMessageDialog(this, text, "Warning", JOptionPane.WARNING_MESSAGE));
    }

    public void updateLabelProgressUpdate(String progressText) {

        SwingUtilities.invokeLater(() -> labelProgressUpdate.setText(progressText));

    }

    private void initUserInterface() {

        this.labelRequiredAction.setText("Required User Action");
        this.buttonDLSSWebsite.setText("Download DLSS");
        this.buttonAddGameDirectory.setText("Add Game Directory");

        this.labelRestoreBackup.setText("Restore");
        this.buttonRestoreBackup.setText("Restore Backup");

        this.labelGitHubLink.setText("https://github.com/YorkshireDev");
        this.buttonReplaceDLSS.setText("Replace DLSS");
        this.labelProgress.setText("Progress");
        this.labelProgressUpdate.setText("N/A");

        this.setContentPane(panelMain);
        this.setTitle("Supersede");
        this.setPreferredSize(new Dimension(480, 240));
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setLocationRelativeTo(null);
        this.pack();
        this.setVisible(true);

    }

    private void initNonUserInterface() {

        Path dlssFolderPath = Path.of("DLSS");
        Path dlssValidGameFilePath = Path.of("Game_List.TXT");

        try {

            if (! Files.exists(dlssFolderPath)) Files.createDirectory(dlssFolderPath);
            if (! Files.exists(dlssValidGameFilePath)) Files.createFile(dlssValidGameFilePath);

        } catch (IOException e) { throw new RuntimeException(e); }

        this.controllerService = Executors.newSingleThreadExecutor();
        isControllerRunning = false;

    }

}
