from signup import SignUp
from fastapi import FastAPI
from option import PhoneDataItem
from typing import List
import uvicorn

app = FastAPI()
signupFace = SignUp()

@app.post('/api/start')
def main(phoneData: List[PhoneDataItem]):
    signupFace.setValue(phoneData, 1)
    message = signupFace.run()
    print ("message ", message)
    return ""

@app.get("/api/action/")
def pause(action:str):
    if(action == "pause"):
        signupFace.action = 0
        print(action)
    elif(action == "continue"):
        signupFace.action = 1
        print(action)
    else:
        signupFace.finish = True

@app.post("/api/test")
def test(phoneData: List[PhoneDataItem]):
    for phone in phoneData:
        print("ID profile = ", phone.idProfile)
        print("Phone Number = ", phone.phoneNumber)

if __name__ == '__main__':
    uvicorn.run(app, host='127.0.0.1', port=8888)  # Định nghĩa cổng 8888


# 8f06aaaa280c027dcf2187f6e93a1c6c


# C:/Users/newlife_pc20/.hidemyacc/profiles/hma_64ddd8d23f04c87649f1b4e8
# C:/Users/newlife_pc20/.hidemyacc/profiles/hma_64ddd8f43f04c87649f1d7c2
# C:/Users/newlife_pc20/.hidemyacc/profiles/hma_64ddd90d3f04c87649f1f095
# C:/Users/newlife_pc20/.hidemyacc/profiles/hma_64ddd9323f04c87649f21403
# C:/Users/newlife_pc20/.hidemyacc/profiles/hma_64ddd9463f04c87649f228e6
# 
# C:\Users\newlife_pc30\AppData\Local\Programs\Python\Python311\Scripts\