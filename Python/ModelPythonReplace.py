import zipfile
from sys import argv
from os import remove


def main(game_dlss_dir: str, replace_dlss_file_dir: str) -> str:

    remove(game_dlss_dir)

    game_dlss_dir_split: list = game_dlss_dir.split("\\")
    game_dlss_dir = ""

    for i in range(len(game_dlss_dir_split) - 1):

        game_dlss_dir += game_dlss_dir_split[i]

        if i < len(game_dlss_dir_split) - 1:
            game_dlss_dir += "\\"

    with zipfile.ZipFile(replace_dlss_file_dir, "r") as dlss_zip_file:
        dlss_zip_file.extractall(game_dlss_dir)

    return str(True)


if __name__ == "__main__":

    result: str = main(argv[1], argv[2])
    print(result)
