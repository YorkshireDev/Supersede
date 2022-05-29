package History;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.file.Files;
import java.nio.file.Path;

class ModelBackup implements Runnable {

    private final String myGame;

    ModelBackup(String myGame) {

        this.myGame = myGame;

    }

    private String spawnPythonProcess(String pythonFile, String pythonArgument) throws IOException, InterruptedException {

        ProcessBuilder pythonProcessBuilder =
                new ProcessBuilder("python", "Python" + File.separator + pythonFile, pythonArgument);

        Process pythonProcess = pythonProcessBuilder.start();

        BufferedReader pythonReader = new BufferedReader(new InputStreamReader(pythonProcess.getInputStream()));

        String pythonOutput = pythonReader.readLine();

        pythonProcess.waitFor();
        pythonProcess.destroy();

        return pythonOutput;

    }

    @Override
    public void run() {

        try {

            String[] gameSplit = myGame.split(">");

            String gameDir = gameSplit[0];
            String gameDLSSVersion = gameSplit[1];
            String gameName = gameSplit[2];

            String currentDLSSVersion = spawnPythonProcess("ModelPythonSearch.py", gameDir);

            if (currentDLSSVersion.equals(gameDLSSVersion)) {

                String backupPath = new File("Backup").getAbsolutePath() + File.separator;

                if (! Files.exists(Path.of(backupPath + gameName + ".zip"))) {

                    String pythonArgument = gameDir + ">" + backupPath + ">" + gameName;

                    String backupSuccess = spawnPythonProcess("ModelPythonBackup.py", pythonArgument);

                    if (!backupSuccess.equals("True")) throw new RuntimeException("Error -> Could Not Write Backup!");
                    // TODO: 29/05/2022 Error Message to View

                }

            }

        } catch (IOException | InterruptedException e) { throw new RuntimeException(e); }

        ControllerBackup.modelBackupLatch.countDown();

    }

}
