package Search;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;

class ModelSearch implements Runnable {

    private final File mySearchDir;
    private String searchResult;

     ModelSearch(File mySearchDir) {

        this.mySearchDir = mySearchDir;
        this.searchResult = null;

    }

    private void setSearchResult(String searchResult) {
        this.searchResult = searchResult;
    }

    String getSearchResult() {
        return searchResult;
    }

    private void recursiveSearch(File currentDir) {

         File[] currentDirList = currentDir.listFiles();

         if (currentDirList != null) {

             for (File dirFile : currentDirList) {

                 if (dirFile.getName().startsWith("nvngx_dlss")) {

                     try {

                         ProcessBuilder pythonProcessBuilder = new ProcessBuilder(
                                 "python",
                                 "Python" + File.separator + "ModelPythonSearch.py",
                                 dirFile.getAbsolutePath());

                         Process pythonProcess = pythonProcessBuilder.start();

                         BufferedReader pythonReader =
                                 new BufferedReader(new InputStreamReader(pythonProcess.getInputStream()));

                         setSearchResult(dirFile.getAbsolutePath() + ">" + pythonReader.readLine() + ">" + mySearchDir.getName());

                         pythonProcess.waitFor();
                         pythonProcess.destroy();

                         return;

                     } catch (IOException | InterruptedException e) { throw new RuntimeException(e); }

                 }

                 if (dirFile.isDirectory()) recursiveSearch(dirFile);

             }

         }

    }

    @Override
    public void run() {

         recursiveSearch(mySearchDir);
         ControllerSearch.modelSearchLatch.countDown();

    }

}
