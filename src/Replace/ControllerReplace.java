package Replace;

import GUI.ViewUserInterface;

import java.io.*;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class ControllerReplace implements Runnable {

    private final ViewUserInterface viewUserInterface;
    private final boolean restoreBackupMode;

    private final ExecutorService modelReplaceService;
    static CountDownLatch modelReplaceLatch;

    public ControllerReplace(ViewUserInterface viewUserInterface, boolean restoreBackupMode) {

        this.viewUserInterface = viewUserInterface;
        this.restoreBackupMode = restoreBackupMode;

        this.modelReplaceService = Executors.newFixedThreadPool(Runtime.getRuntime().availableProcessors());

    }

    @Override
    public void run() {

        ViewUserInterface.isControllerRunning = true;

        try {

            List<ModelReplace> modelReplaceList = new ArrayList<>();

            String dlssRootDir = new File(restoreBackupMode ? "Backup" : "DLSS").getAbsolutePath();

            BufferedReader gameListReader = new BufferedReader(new FileReader("Game_List.TXT"));
            String gameLine;

            while ((gameLine = gameListReader.readLine()) != null)
                modelReplaceList.add(new ModelReplace(dlssRootDir, gameLine));

            Collections.shuffle(modelReplaceList);

            int replaceAmount = modelReplaceList.size();

            modelReplaceLatch = new CountDownLatch(replaceAmount);

            for (ModelReplace modelReplace : modelReplaceList) modelReplaceService.submit(modelReplace);

            while (modelReplaceLatch.getCount() > 0) {

                long searchedDirs = replaceAmount - modelReplaceLatch.getCount();
                String progressText = searchedDirs + "/" + replaceAmount;
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
