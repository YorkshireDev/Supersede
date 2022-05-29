from win32api import GetFileVersionInfo, LOWORD, HIWORD
from sys import argv


def main(dlss_dir: str) -> str:

    file_version_info = GetFileVersionInfo(dlss_dir, "\\")

    m_s = file_version_info["FileVersionMS"]  # Most Significant
    l_s = file_version_info["FileVersionLS"]  # Least Significant

    # Version Grammar: m_s . m_s . l_s . l_s or X.x.Y.y

    dlss_version: str = str(HIWORD(m_s)) + "." + str(LOWORD(m_s)) + "." + str(HIWORD(l_s)) + "." + str(LOWORD(l_s))

    return dlss_version


if __name__ == "__main__":

    result: str = main(argv[1])
    print(result)

