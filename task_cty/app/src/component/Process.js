import React, { Component } from 'react'
// import React, { useState, useEffect } from 'react';
import './sass/process.scss';
import '..//../node_modules/bootstrap/dist/css/bootstrap.min.css'
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome'
import axios from 'axios'
import { faMicrochip, faPowerOff, faCirclePause, faCircleMinus, faCirclePlay } from '@fortawesome/free-solid-svg-icons'
import { json } from 'react-router-dom';
export class Process extends Component {
    constructor() {
        super()
        // sessionStorage.setItem("processData", JSON.stringify([]))
        this.intervalId = null
        this.state = {
            // currentSim: localStorage.getItem("currentSim") == null ? {} : JSON.parse(localStorage.getItem("currentSim")),
            // intervalId: null,
            // processData: localStorage.getItem("processData") == null ? [] : JSON.parse(localStorage.getItem("processData")),
            processData : localStorage.getItem("processData") == null ? [] : JSON.parse(localStorage.getItem("processData")),
            currentIcon: faCirclePause
        }
        this.pauseContinueStatus = "continue"
    }
    // addProcess = (processData, currentProcess) => {
    //     const index = processData.findIndex(process => process.current_phoneNumber === currentProcess.current_phoneNumber)
    //     if (index !== -1) {
    //         processData[index] = currentProcess
    //         // processData.pop()
    //         // processData.push(currentProcess)
    //     }
    //     else processData.push(currentProcess)
    //     return processData
    // }
    startProgress = () => {
        // let config = JSON.parse(sessionStorage.getItem("configData"))
        if (sessionStorage.getItem("status") != 1) {
            localStorage.clear()
            try {
                sessionStorage.setItem("running", 1)
                // console.log("config = " + config)
                axios.get("http://localhost:8081/api/play")
                    .then(respone => {
                        if(respone.data != '') {
                            sessionStorage.setItem("running", 0)
                        }
                        alert(respone.data)
                        
                    })
            }
            catch (error) {
                console.log("Number exception")
            }
            if(sessionStorage.getItem("running") == 1) {
                this.setState({
                    // currentSim:{},
                    processData:[],
                    currentIcon: faCirclePause
                })
                this.componentDidMount()
            }
        }
        else {
            alert("Bạn chưa cấu hình cho tiến trình")
        }
    }
    pauseOrContinueOrFinish = (action) => {
        let running = sessionStorage.getItem("running")
        console.log("running " + running)
        if (running == 1) {
            if (action != "finish")
                this.changeIconPauseContinue()
            axios.get('http://localhost:8081/api/action?action=' + action)
                .then(respone => {
                    console.log(respone)
                })
        }
        else alert("Tiến trình chưa được bắt đầu")

    }
    changeIconPauseContinue = () => {
        this.setState(prevState => ({
            currentIcon: prevState.currentIcon === faCirclePause ? faCirclePlay : faCirclePause
        }))
    }
    isEmptyObject = (obj) => {
        for (let key in obj) {
          if (obj.hasOwnProperty(key)) {
            return false;
          }
        }
        return true;
      }
    componentDidMount = () => {
        sessionStorage.setItem("running", 1)
        if (sessionStorage.getItem("running") == 1) {
            this.intervalId =
                setInterval(  () => {
                    axios.get('http://localhost:8081/api/process_current')
                    .then(respone => {
                        return respone.data
                    })
                    .then(respone => {
                        console.log(respone)
                        if (respone!==null && !this.isEmptyObject(respone)) {
                            console.log(respone)
                            let newProcessData = respone
                            let currentSim = respone[respone.length-1]
                            this.setState({
                                // currentSim: currentSim,
                                processData: newProcessData==null?[]:newProcessData
                            })
                            localStorage.setItem("processData", JSON.stringify(newProcessData))
                            // localStorage.setItem("currentSim", JSON.stringify(respone))
                        }
                        if (respone.check == true) {
                            alert("Tiến trình đã hoàn thành!!")
                            this.componentWillUnmount()
                            sessionStorage.setItem("running", 0)
                            sessionStorage.setItem("status", 1)
                            sessionStorage.clear()
                        }
                    })
                },3 * 1000)
        }
    }
    componentWillUnmount = () => {
        clearInterval(this.intervalId)
    }

    render() {
        const currentSim = this.state.processData.length > 0 ?this.state.processData[this.state.processData.length-1] :{}
        const processData = this.state.processData
        // console.log( currentSim)
        const currentIcon = this.state.currentIcon
        return (
            <div className="process-container">
                <div className="header">
                    <div className="header-symbol">
                        <FontAwesomeIcon icon={faMicrochip} style={{ color: "#ffffff", fontSize: "2rem", padding: "10px" }} />
                    </div>
                    <div className="header-text">Tiến trình</div>
                </div>
                <main>
                    <div className="row mt-3 btns-container ">
                        <div className="col-md-4 text-center btn-container ">
                            <button className="btn-container-green">
                                <FontAwesomeIcon icon={faPowerOff}
                                    style={{
                                        fontSize: "2rem",
                                        color: "#24c79f"
                                    }}
                                    onClick={this.startProgress}
                                />
                            </button>
                        </div>
                        <div className="col-md-4 text-center btn-container">
                            <button className="btn-container-yellow">
                                <FontAwesomeIcon icon={currentIcon} id="pauseOrContinue"
                                    style={{
                                        fontSize: "2rem",
                                        color: "#d0dc45"
                                    }}
                                    onClick={() => {
                                        this.pauseContinueStatus = this.pauseContinueStatus == "continue" ? "pause" : "continue"
                                        this.pauseOrContinueOrFinish(this.pauseContinueStatus);
                                    }}
                                />
                            </button>
                        </div>
                        <div className="col-md-4 text-center btn-container">
                            <button className="btn-container-red">
                                <FontAwesomeIcon icon={faCircleMinus}
                                    style={{
                                        fontSize: "2rem",
                                        color: "#cc3f3f"
                                    }}
                                    onClick={() => {
                                        this.pauseOrContinueOrFinish("finish");
                                        sessionStorage.setItem("running", 0)
                                        sessionStorage.setItem("status", 0)
                                    }}
                                />
                            </button>
                        </div>
                    </div>
                    <div className="row mt-3 mb-3 progress-container">
                        <div className="text-white " style={{ fontSize: "1.2rem" }}>
                            Tiến độ thực hiện
                        </div>
                        <div className="progress mt-2" style={{ padding: 0 }}>
                            <div
                                className="progress-bar"
                                style={{ width: currentSim.process }}
                                role="progressbar"
                                aria-valuenow={22}
                                aria-valuemin={0}
                                aria-valuemax={100}
                            >
                            </div>
                            <div className="progress-text" style={{ left: "50%" }}>
                                <span>{`${currentSim.index ? currentSim.index + "/" : ""} `}</span><span>{currentSim.totalPhoneNumber}</span>
                            </div>
                        </div>
                    </div>
                    <table className="mt-4 table">
                        <thead className="text-center">
                            <tr>
                                <th>STT</th>
                                <th>Sim duyệt</th>
                                <th>Trạng thái</th>
                                <th>Thông báo</th>
                            </tr>
                        </thead>
                        <tbody className="text-center" >
                            {
                                processData.map((sim, index) => {
                                    // console.log(sim)
                                        return (
                                            <tr key={index}>
                                                <td>{index+1}</td>
                                                <td>{sim.current_phoneNumber}</td>
                                                <td>{sim.status}</td>
                                                <td>{sim.message}</td>
                                            </tr>
                                        )
                                    })
                            }
                        </tbody>
                    </table>

                </main>

            </div>
        )
    }
}
export default Process