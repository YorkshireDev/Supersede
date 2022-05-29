package Replace;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;

class ModelReplace implements Runnable {

    private final String dlssRootDir;
    private final String myGameList;

    ModelReplace(String dlssRootDir, String myGameList) {

        this.dlssRootDir = dlssRootDir;
        this.myGameList = myGameList;

    }

    @Override
    public void run() {

        String[] myGameListSplit = myGameList.split(">");

        String gameDir = myGameListSplit[0];
        String gameDLSSFile = myGameListSplit[2] + ".zip";

        File[] dlssRootDirArray = new File(dlssRootDir).listFiles();

        if (dlssRootDirArray != null) {

            if (dlssRootDir.endsWith("DLSS")) gameDLSSFile = dlssRootDirArray[0].getAbsolutePath();
            else {

                for (File backupDLSSZip : dlssRootDirArray) {

                    if (backupDLSSZip.getName().equals(gameDLSSFile)) {

                        gameDLSSFile = backupDLSSZip.getAbsolutePath();
                        break;

                    }

                }

            }

            try {

                ProcessBuilder pythonProcessBuilder =
                        new ProcessBuilder("python", "Python" + File.separator + "ModelPythonReplace.py", gameDir, gameDLSSFile);

                Process pythonProcess = pythonProcessBuilder.start();

                BufferedReader pythonReader = new BufferedReader(new InputStreamReader(pythonProcess.getInputStream()));

                if (! pythonReader.readLine().equals("True")) throw new RuntimeException("Error -> Could Not Replace DLSS!");

                pythonProcess.waitFor();
                pythonProcess.destroy();

            } catch (IOException | InterruptedException e) { throw new RuntimeException(e); }

        }

        ControllerReplace.modelReplaceLatch.countDown();

    }

}
