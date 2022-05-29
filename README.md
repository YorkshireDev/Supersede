# Supersede
Project Supersede

---

# Summary

Supersede is an Nvidia DLSS DLL replacement program written in Java and Python with a simple user interface. It works by taking the DLSS zip file taken from [TechPowerUp](https://www.techpowerup.com/download/nvidia-dlss-dll/), making a backup of the games' shipping DLSS version, and then replacing them all with the downloaded DLSS file, with an option to restore them all to their original versions if wanted.

---

# Requirements & Setup

1. **Windows**\*
2. **Java 11** Runtime or Greater
3. **Python 3.10** or Greater

\* Developed and Tested on Windows 10 21H2 64-bit

The program requires a Python library to function. To install the necessary components, follow these steps:

1. Open a terminal in the same directory as the `requirements.txt` file
2. Type `pip install -r requirements.txt`

If Python is not installed to your system PATH, there are plenty of guides that tell you how to add it

---

# Features (Current)

1. "Download DLSS" button that takes you directly to the [TechPowerUp DLSS download page](https://www.techpowerup.com/download/nvidia-dlss-dll/)

3. "Add Game Directory" button that allows you to bulk-add games from a root directory, e.g., the Steam "common" folder, DLSS 2.0 game detection is automated

5. "Replace DLSS" button that backs up and replaces the games' DLSS files that you selected earlier

7. "Restore Backup" button that restores all the games back to their original DLSS versions that were backed up earlier

9. A real-time updating progress dialogue, showing the progress of backing up, replacing or restoring DLSS files

11. All four primary operations (adding games, backing up, replacing, and restoring) are multithreaded

---

# Features (Upcoming)

1. Ability to add single games instead of bulk-adding directories of games

3. Linux support if DLSS comes natively to Linux

---

# Usage

1. Double-click the `Supersede.jar` file provided, or open up a terminal and type `java -jar Supersede.jar`

2. Click the "Download DLSS" button, and download the latest/desired DLSS version from TechPowerUp

3. Place the downloaded zip file in the "DLSS" folder inside the program directory

4. Click "Add Game Directory" and navigate to the folder where all your games are. If you have Steam for example, this would be the "common" folder, select this

5. Do this for as many different game directories you have (e.g., Steam on different drives, Origin games, Battle.NET games, etc)

6. The program will automatically go through all the games in a directory and add them if they are DLSS 2.0 compatible

7. Click the "Replace DLSS" button and it will update all of your games to the DLSS version you placed in the DLSS folder earlier

8. To update to a newer version later, simply replace the zip file in the DLSS folder with a new one, and click the "Replace DLSS" button again

9. To restore your games to their original DLSS versions, click the "Restore Backup" button

**NOTE: If your DLSS games are in an administrator protected directory (such as Program Files (x86)) you will need to run this program as an administrator, either through an administrator-elevated terminal or by giving the `Supersede.jar` file the appropriate WRITE permissions!**
