import time
import requests
from option import Option
from option import PhoneDataItem
from datetime import datetime
from selenium import webdriver
from selenium.webdriver.common.by import By
from selenium.webdriver.support import expected_conditions as EC
from selenium.webdriver.support.wait import WebDriverWait
from selenium.common.exceptions import NoSuchFrameException
from selenium.common.exceptions import NoSuchElementException

class SignUp:
    def __init__(self) -> None:
        pass

    def setValue(self, phoneNumbers, action):
        self.phoneNumbers = phoneNumbers
        self.action = action
        self.finish = False

    def stop(self):
        while(self.action != 1 and self.finish == False):
            time.sleep(5)

    def close_browser(self, driver):
        driver.delete_all_cookies()
        driver.quit()

    # random name
    def ranDomInfor(self):
        api_url = 'https://api.api-ninjas.com/v1/randomuser'
        response = requests.get(api_url, headers={'X-Api-Key': 'GqP5lERc6UrXm0FHlCe2Qg==Hvy72QleRLRlN9GJ'})
        if response.status_code == requests.codes.ok:
            infor ={}
            infor["names"] = response.json()['name'].split()
            infor["birthday"] = response.json()['birthday'].split("-")
            return infor
        else:
            print("Error:", response.status_code, response.text)

    # sign up face
    def signupface(self, driver, phoneNumber, index):
        driver.get("https://www.facebook.com")
        # time.sleep(200)
        try:
            # click dang ki
            self.stop()
            if(self.finish == True):
                self.close_browser(driver)
                return "Đã kết thúc"
            btnSignUp = driver.find_element(By.XPATH, '/html/body/div[1]/div[1]/div[1]/div/div/div/div[2]/div/div[1]/form/div[5]/a')
            btnSignUp.click() 
        except NoSuchElementException:
            print("Can not found button create account")
        time.sleep(10)
        # fillout form
        infor = self.ranDomInfor()
        print(infor)
        fillOutForm(driver, phoneNumber, infor)
        # click submit form sign up
        print("Sign up account")
        self.stop()
        if(self.finish == True):
            self.close_browser(driver)
            return 'finish'
        print("finish ", self.finish)
        # click đăng ký
        driver.find_element(By.NAME, 'websubmit').click()
        time.sleep(20)
        try:
            btnSignUp = driver.find_element(By.NAME, 'websubmit')
            return "Error"
        except NoSuchElementException:
            print("OK!")
        try: 
            driver.find_element(By.XPATH, "/html/body/div[5]/div[2]/div/div/div/div[3]/a[1]").click()
        except NoSuchElementException:
            print("Can not message notify: 'Bạn đã có tài khoản chưa?' ")
        time.sleep(20)
        # check have error with phone number register
        try:
            message = driver.find_element(By.CLASS_NAME, '_58mo').text
            print("Message error ", message)
            if(message != ''):
                self.close_browser(driver)
                self.sendProcess(phoneNumber, "Số điện thoại đã tồn tại", index, "Thất bại")
                return None
        except NoSuchElementException :
            print("OK, not error with phone number")
        return ""

    # Send progress resolving
    def sendProcess(self, current_phoneNumber, message, index, status):
        urlJava = "http://localhost:8081/api/process"
        data = {
            'current_phoneNumber':  current_phoneNumber,
            'message': message,
            'index': index,
            'status': status
        }
        print("current_phoneNumber: ", data['current_phoneNumber'])
        print("status: ", data['status'])
        if(status == ""):
            self.sendTimeSend()
            time.sleep(3)

        response = requests.post(urlJava, json=data)
        print("Response api java ", response)

    # send time start request sms
    def sendTimeSend(self):
        urlJava = "http://localhost:8081/api/time_send"
        time = datetime.now()
        print(time.strftime("%Y-%m-%d %H:%M:%S"))
        requests.post(urlJava, params={"time": time.strftime("%Y-%m-%d %H:%M:%S")})

    #  config profile with phoneNumber
    def createOption(self, profilePath):
        options = Option(profilePath)
        option = options.createOption()
        return option

   # add api key
    def addApiKey(self, driver, apiKey):
        try: 
            input_apiKey = driver.find_element(By.NAME, "apiKey")
            input_apiKey.send_keys(apiKey)
            driver.find_element(By.ID,"connect").click()
            wait = WebDriverWait(driver, 10)
            alert = wait.until(EC.alert_is_present())
            alert.accept()
        except NoSuchElementException:
            print("Đã có extention ")
            
    # main
    def run(self):
        for i, phoneNumber in enumerate(self.phoneNumbers, start=1):
            check_resolve_captcha = True
            check_send_Sms = False
            check_profile = False
            count_loop = 0
            message = ""
            print("Working on phone number {i} = ", phoneNumber.phoneNumber)
            driver = None
            # solve byPass captcha
            while True and self.action == 1 and self.finish == False:
                count_loop += 1
                if(count_loop == 5):
                    self.sendProcess(phoneNumber.phoneNumber, "Lỗi profile", i, "Thất bại")
                    check_profile = True
                    break

                self.stop()
                if(self.finish == True):
                    self.close_browser(driver)
                    return "Đã kết thúc"
                try:
                    self.close_browser(driver)
                except Exception:
                    print("Chưa có driver!")

                self.sendProcess(phoneNumber.phoneNumber, "Đang thực hiện", i, "Đang thực hiện")
                driver = webdriver.Chrome(options=self.createOption(phoneNumber.profile))
                time.sleep(5) 
                # add extention and api key
                try:
                    self.stop()
                    if(self.finish == True):
                        self.close_browser(driver)
                        return "Đã kết thúc"
                    self.addApiKey(driver, phoneNumber.apiKey)
                except Exception as e:
                    print(str(e))

                time.sleep(10)
                try:
                    message = self.signupface(driver, phoneNumber.phoneNumber, i)
                    if(message == None or message == "finish"):
                        break
                    if(message == "Error"):
                        self.sendProcess(phoneNumber.phoneNumber, "Lỗi profile", i, "Thất bại")
                        check_profile = True
                        break
                    # click button continue if exists
                    time.sleep(20)
                    try:
                        self.stop()
                        if(self.finish == True):
                            self.close_browser(driver)
                            return "Đã kết thúc"
                        btnContinue = driver.find_element(By.XPATH, '/html/body/div/div/div/div/div/div/div/div/div[2]/div/div/div/div[1]/div/div/div[1]/div/div/div/div/div/div/div/div[2]/div/div/div[2]/div/div/div')
                        btnContinue.click()
                    except NoSuchElementException:
                        print("Can't not found button continue")
                    time.sleep(20)
                    # click appeal
                    try:
                        btn = driver.find_element(By.XPATH, "/html/body/div/div/div/div/div/div/div/div/div[2]/div/div/div/div[1]/div/div/div[1]/div/div/div/div/div/div/div/div[2]/div/div/div/div")
                        btn.click()
                    except NoSuchElementException:
                        print("Can not found appeal")

                    # click button to Bypass captcha use api of 2 captcha
                    time.sleep(20)
                    try:
                        driver.switch_to.frame('captcha-recaptcha')
                        time.sleep(20)
                        self.stop()
                        if(self.finish == True):
                            self.close_browser(driver)
                            return "Đã kết thúc"
                        resolveCaptcha = driver.find_element(By.XPATH, '/html/body/div[1]/div[2]/div[2]')
                        resolveCaptcha.click()
                        time.sleep(70)
                        driver.switch_to.default_content()
                        try:
                            # click continue when Bypass captcha success
                            time.sleep(30)
                            self.stop()
                            if(self.finish == True):
                                self.close_browser(driver)
                                return "Đã kết thúc"
                            conti = driver.find_element(By.XPATH, '/html/body/div/div/div/div/div/div/div/div/div[2]/div/div/div/div[1]/div/div/div[1]/div/div/div/div/div/div/div/div[3]/div/div/div')
                            aria_disabled_value = conti.get_attribute("aria-disabled")
                            if(aria_disabled_value == True):
                                if(self.finish == True):
                                    self.close_browser(driver)
                                    return "Đã kết thúc"
                                self.sendProcess(phoneNumber.phoneNumber, "Thất bại", i, "Lỗi giải captcha sai!")
                                check_resolve_captcha = False
                                break
                            conti.click()
                            self.sendProcess(phoneNumber.phoneNumber, "Đang chờ SMS", i, "")
                            check_send_Sms = True
                            break
                        except NoSuchElementException: 
                            print('Can not ByPass captcha')
                            if(self.finish == True):
                                self.close_browser(driver)
                                return "Đã kết thúc"
                            self.sendProcess(phoneNumber.phoneNumber, "Thất bại", i, "Lỗi giải captcha sai!")
                            check_resolve_captcha = False
                            break

                    except NoSuchElementException:
                        print("No need to by pass captcha")
                    except NoSuchFrameException:
                        print("Error switch to frame!")
                        break

                # Can not found any element in website
                except NoSuchElementException : 
                    print('Error can not found element!')   
                    self.close_browser(driver)
            self.stop()
            if(self.finish == True):
                self.close_browser(driver)
                return "Đã kết thúc"
            # check byPass captcha or check profile
            if(check_resolve_captcha == False or check_profile):
                self.close_browser(driver)
                time.sleep(5)
                continue
            # case phone number exsist
            if(message == None):
                time.sleep(10)
                continue
            elif(message == 'Đã kết thúc'):
                return 'Đã kết thúc'
            # solve send SMS again
            time.sleep(10)
            try:
                self.stop()
                if(self.finish == True):
                    self.close_browser(driver)
                    return "Đã kết thúc"
                sendSMSAgain = driver.find_element(By.CLASS_NAME, '_1w00 _8iu7')
                sendSMSAgain.click()
                time.sleep(10)
                driver.find_element(By.XPATH, "/html/body/div[6]/div[2]/div/div/div/div[3]/a").click()
            except NoSuchElementException:
                print("No need to Send SMS Again!")

            # Resolve to re-enter the phone number and require send SMS
            time.sleep(10)
            try:
                self.stop()
                if(self.finish == True):
                    self.close_browser(driver)
                    return "Đã kết thúc"
                driver.find_element(By.XPATH, '/html/body/div/div/div/div/div/div/div/div/div[2]/div/div/div/div[1]/div/div/div[1]/div/div/div/div/div/div/div/div[2]/div/label/div/div[2]/input').send_keys(phoneNumber.phoneNumber)
                sendSMS = driver.find_element(By.XPATH, '/html/body/div/div/div/div/div/div/div/div/div[2]/div/div/div/div[1]/div/div/div[1]/div/div/div/div/div/div/div/div[4]/div/div/div')
                print("Button Send SMS ", sendSMS)
                sendSMS.click()
                if(not check_send_Sms): # Case of checkpoint ==> send again phone number
                    self.sendTimeSend()
                # time.sleep(50)
                # resend 
                # /html/body/div/div/div/div/div/div/div/div/div[2]/div/div/div/div[1]/div/div/div[1]/div/div/div/div/div/div/div/div[3]/div/div[1]/span/div
            except NoSuchElementException:
                print("Check point error")

            # wait SMS send to phoneNumber
            if(not check_send_Sms):
                self.sendProcess(phoneNumber.phoneNumber, "Đang chờ SMS", i, "")
            self.stop()
            if(self.finish == True):
                self.close_browser(driver)
                return "Đã kết thúc"
            time.sleep(300) # chờ 2 phút
            self.close_browser(driver)
        return "Đã hoàn thành"
    
# fill out infor user
def fillOutForm(driver, phoneNumber, infor):
    for i in range(0, 2):
        if(infor["names"][i] == ""):
            infor["names"][i] = "Hung"

    driver.find_element(By.NAME, 'lastname').send_keys(infor["names"][0])# Ho
    driver.find_element(By.NAME, 'firstname').send_keys(infor["names"][1]) # ten
    driver.find_element(By.NAME, 'reg_email__').send_keys(phoneNumber) # so dien thoai
    driver.find_element(By.NAME, 'reg_passwd__').send_keys('123456789@') # mat khau
    driver.find_element(By.NAME, 'birthday_day').send_keys(infor["birthday"][2]) # ngay sinh
    driver.find_element(By.NAME, 'birthday_month').send_keys('Jan') # thang sinh
    driver.find_element(By.NAME, 'birthday_year').send_keys(infor["birthday"][0]) # nam sinh
    driver.find_element(By.NAME, 'sex').click() # gioi tinh (nam)



# if __name__ == '__main__':
#     listPhoneNumber = ['0769045906', '0782357678', '0782353979', '0769119699', '0769118787', '0769108833',
#     '0769098833' ,'0769097567' , '0769078877', '0769078822', '0769078811']
#     profilePath = "C:/Users/ASUS/.hidemyacc/profiles/profile 2"
#     options = Option(profilePath)
#     option = options.createOption()
#     signUp = SignUp(listPhoneNumber, option)
#     signUp.run()

# try: 
#     # click reSend SMS
#     time.sleep(10)
#     sendSMSAgain1 = driver.find_element(By.XPATH, '/html/body/div/div/div/div/div/div/div/div/div[2]/div/div/div/div[1]/div/div/div[1]/div/div/div/div/div/div/div/div[3]/div/div[1]/span')
#     sendSMSAgain2 = driver.find_element(By.XPATH, '/html/body/div/div/div/div/div/div/div/div/div[2]/div/div/div/div[1]/div/div/div[1]/div/div/div/div/div/div/div/div[3]/div/div[1]/span/div')
#     # sendSMSAgain.click()
#     print('sendSMSAgain1', sendSMSAgain1)
#     print('sendSMSAgain2', sendSMSAgain2)
# except NoSuchElementException:
#     print("Check point error, Can not resend SMS")