import time
from selenium import webdriver
from selenium.webdriver.chrome.options import Options
from selenium.webdriver.chrome.service import Service as ChromeService

def open_facebook_on_profile(profile_path, url):
    # Khởi tạo Chrome Options
    chrome_options = Options()

    # Chỉ định đường dẫn tới profile đã chỉ định
    chrome_options.add_argument("--user-data-dir=C:/Users/newlife_pc20/.hidemyacc/profiles/hma_64dca1300385c4ba1dc9705c")
    chrome_options.add_argument("--profile-directory=Default")
    chrome_options.add_argument('load-extension=C:/Users/newlife_pc20/.hidemyacc/extensions/user-extensions/ezyZip.crx')

    # Khởi tạo trình điều khiển Chrome với các tùy chọn đã định
    driver = webdriver.Chrome(options=chrome_options)
    # driver = ChromeService(executable_path="", chrome_options)

    # Mở liên kết Facebook trên profile đã chỉ định
    driver.get(url)
    # time.sleep(20)
    input(" ")

if __name__ == "__main__":
    # Chỉ định đường dẫn tới profile bạn muốn sử dụng
    profile_path = "C:/Users/ASUS/AppData/Local/Google/Chrome/User Data/Profile 12"

    # Chỉ định URL của Facebook bạn muốn mở
    facebook_url = ""

    # Gọi hàm để mở Facebook trên profile đã chỉ định
    open_facebook_on_profile(profile_path, facebook_url)

    