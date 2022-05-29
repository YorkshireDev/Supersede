package History;

import GUI.ViewUserInterface;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class ControllerBackup implements Runnable {

    private final ViewUserInterface viewUserInterface;

    private final ExecutorService modelBackupService;
    static CountDownLatch modelBackupLatch;

    public ControllerBackup(ViewUserInterface viewUserInterface) {

        this.viewUserInterface = viewUserInterface;
        this.modelBackupService = Executors.newFixedThreadPool(Runtime.getRuntime().availableProcessors());

    }

    @Override
    public void run() {

        ViewUserInterface.isControllerRunning = true;

        try {

            Path backupPath = Path.of("Backup");
            if (! Files.exists(backupPath)) Files.createDirectory(backupPath);

            List<ModelBackup> modelBackupList = new ArrayList<>();

            BufferedReader gameListReader = new BufferedReader(new FileReader("Game_List.TXT"));
            String gameLine;

            while ((gameLine = gameListReader.readLine()) != null) modelBackupList.add(new ModelBackup(gameLine));

            Collections.shuffle(modelBackupList);

            int gameAmount = modelBackupList.size();
            modelBackupLatch = new CountDownLatch(gameAmount);

            for (ModelBackup modelBackup : modelBackupList) this.modelBackupService.submit(modelBackup);

            while (modelBackupLatch.getCount() > 0) {

                long searchedDirs = gameAmount - modelBackupLatch.getCount();
                String progressText = searchedDirs + "/" + gameAmount;
                viewUserInterface.updateLabelProgressUpdate(progressText);

                try {
                    Thread.sleep(1L);
                } catch (InterruptedException e) { throw new RuntimeException(e); }

            }

            viewUserInterface.updateLabelProgressUpdate("Done!");

        } catch (IOException e) { throw new RuntimeException(e); }

        ViewUserInterface.isControllerRunning = false;

    }

}
