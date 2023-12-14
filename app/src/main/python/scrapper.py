import time
from selenium import webdriver
from selenium.webdriver.chrome.service import Service as ChromeService
from selenium.webdriver.common.by import By
from webdriver_manager.chrome import ChromeDriverManager

def main():
    url = "https://mangasee123.com/read-online/Return-of-the-Blossoming-Blade-chapter-86.html"
    options = webdriver.ChromeOptions()
    src_list = []
    with webdriver.Chrome(service=ChromeService(ChromeDriverManager().install()), options=options) as driver:
        # driver.get(url)
        # time.sleep(2)
        #
        # loginBox = driver.find_elements(By.CSS_SELECTOR, 'img.NoGap, img.HasGap, img-fluid')
        #
        # time.sleep(4)

        # for item in loginBox:
        #     src_list.append(item.get_attribute("src"))

        return src_list

