import zipfile
from sys import argv


def main(dir_list_str: str) -> str:

    dir_list_split: list = dir_list_str.split(">")

    game_dlss_dir: str = dir_list_split[0]
    backup_file_name: str = dir_list_split[1] + dir_list_split[2] + ".zip"

    with zipfile.ZipFile(backup_file_name, "w", compression=zipfile.ZIP_LZMA, compresslevel=9) as backup_zip:
        backup_zip.write(game_dlss_dir, arcname="nvngx_dlss.dll")

    return str(True)


if __name__ == "__main__":

    result: str = main(argv[1])
    print(result)
