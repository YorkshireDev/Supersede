package Search;

import GUI.ViewUserInterface;

import java.io.*;
import java.util.*;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class ControllerSearch implements Runnable {

    private final ViewUserInterface viewUserInterface;

    private final File gameRootDir;

    private final ExecutorService modelSearchService;
    static CountDownLatch modelSearchLatch;

    public ControllerSearch(ViewUserInterface viewUserInterface, File gameRootDir) {

        this.viewUserInterface = viewUserInterface;
        this.gameRootDir = gameRootDir;
        this.modelSearchService = Executors.newFixedThreadPool(Runtime.getRuntime().availableProcessors());

    }

    @Override
    public void run() {

        ViewUserInterface.isControllerRunning = true;

        File[] dirList = gameRootDir.listFiles();

        if (dirList != null) {

            int dirAmount = dirList.length;

            List<String> searchResultList = new ArrayList<>();

            ModelSearch[] modelSearchArray = new ModelSearch[dirAmount];
            modelSearchLatch = new CountDownLatch(dirAmount);

            for (int i = 0; i < dirAmount; i++) {
                modelSearchArray[i] = new ModelSearch(dirList[i]);
                this.modelSearchService.submit(modelSearchArray[i]);
            }

            while (modelSearchLatch.getCount() > 0) {

                long searchedDirs = dirAmount - modelSearchLatch.getCount();
                String progressText = searchedDirs + "/" + dirAmount;
                viewUserInterface.updateLabelProgressUpdate(progressText);

                try {
                    Thread.sleep(1L);
                } catch (InterruptedException e) { throw new RuntimeException(e); }

            }

            viewUserInterface.updateLabelProgressUpdate("Done!");

            for (ModelSearch modelSearch : modelSearchArray) {

                String searchResult = modelSearch.getSearchResult();
                if (searchResult != null) searchResultList.add(searchResult);

            }

            try {

                BufferedReader gameListReader = new BufferedReader(new FileReader("Game_List.TXT"));
                Set<String> gameListSet = new HashSet<>();

                String gameLine;
                while ((gameLine = gameListReader.readLine()) != null) gameListSet.add(gameLine.split(">")[0]);

                BufferedWriter gameListWriter = new BufferedWriter(new FileWriter("Game_List.TXT", true));

                for (String searchResult : searchResultList) {

                    String[] searchResultSplit = searchResult.split(">");

                    if (gameListSet.contains(searchResultSplit[0])) continue;
                    if (searchResultSplit[1].startsWith("1")) continue; // No DLSS 1.x

                    gameListWriter.write(searchResult);
                    gameListWriter.newLine();

                }

                gameListWriter.flush();

                gameListWriter.close();

            } catch (IOException e) { throw new RuntimeException(e); }

        }

        ViewUserInterface.isControllerRunning = false;

    }

}
