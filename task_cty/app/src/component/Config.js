import React, { Component } from 'react'
import './sass/config.scss';
import '..//../node_modules/bootstrap/dist/css/bootstrap.min.css'
import axios from 'axios'
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome'
import { faToolbox, faUser, faKey, faSimCard,faPlus } from '@fortawesome/free-solid-svg-icons'
export class Config extends Component {
    constructor(){
        super()
        this.apiKey = ""
        this.state ={
            listSim: [],
            listApiKey:[],
            profile: ""
        }
    }
    addConfigInfo= async (apiKey)=>{
        await axios.post("http://localhost:8081/api/config/apiKey?apiKey="+apiKey)
            .then(response=>{
                console.log(response.data)
                alert("Lưu cấu hình thành công")
                this.componentDidMount()
            })
    }
    // fillSimListSelection = async ()=>{
    //     var listSim;
    //     await axios.get("http://localhost:8081/api/listsim")
    //         .then(response=>{
    //             listSim = response.data && response ? response.data: []    
    //         })
    //     this.setState(preState =>(
    //         {
    //             ...preState,
    //             listSim:listSim
    //         }
    //     ))
    // }
    fillProFileSelection = async ()=>{
        var listApiKey;
        await axios.get("http://localhost:8081/api/apiKey")
            .then(response=>{
                listApiKey = response.data && response ? response.data: []    
            })
        this.setState(prevState=>(
            {
                ...prevState,
                listApiKey:listApiKey
            }
        ))
    }
    getProfile =  async ()=>{
        await axios.post("http://localhost:8081/api/config/auto")
            .then(response =>{
                alert(response.data)
            })
    }
    componentDidMount = async () =>{
        document.getElementById("newApiKey").value = ""
        // await this.fillProFileSelection()
        if(sessionStorage.getItem("running")==1){
            document.getElementById("apiKey").setAttribute("disabled", "true");
            // document.getElementById("select-sim").setAttribute("disabled", "true");
        }
    }
    
    render() {
        const {listApiKey,listSim } = {...this.state}
        return (
            <div className='config-container'>
                <div className="header">
                    <div className="header-symbol">
                        <FontAwesomeIcon icon={faToolbox} style={{ color: "#ffffff", fontSize: "2rem", padding: "10px" }} />
                    </div>
                    <div className="header-text">Cấu hình</div>
                </div>
                <main>
                    <form className="form-submit">
                        <div className="form-group mb-5 mt-3 d-flex ">
                            <button className='btn profile-btn ' 
                                onClick={this.getProfile}

                            >Cấu hình profile</button>
                        </div>
                        <div className="form-group mb-5 mt-3 d-flex ">
                            <label
                                htmlFor="newApiKey"
                                className="col-form-label back-ground-label br"
                            >
                                <FontAwesomeIcon icon={faKey} style={{ color: "#ffffff", fontSize: 22 }} />
                            </label>
                            <input
                                type="text"
                                className="form-control back-ground-input border border-dark"
                                id="newApiKey"
                                placeholder="Thêm mới apiKey tại đây...."
                                onBlur={(e)=>{
                                    if (e.target.value){
                                        document.getElementById("apiKey").setAttribute("disabled", "true");
                                        // document.getElementById("select-sim").setAttribute("disabled", "true");
                                    }
                                }}
                            />
                        </div>
                        {/* <div className="form-group mb-5 d-flex ">
                            <label
                                htmlFor="apiKey"
                                className="col-form-label back-ground-label br"
                            >
                                <FontAwesomeIcon icon={faUser} style={{ color: "#ffffff", fontSize: 22 }} />
                            </label>
                            <select
                                className="form-control back-ground-input border border-dark"
                                id="apiKey"
                                onBlur={(e)=>{
                                    if(e.target.value!=""){ 
                                        // document.getElementById("newProfile").setAttribute("disabled","true")
                                        document.getElementById("newApiKey").setAttribute("disabled","true")
                                    }
                                    else{ 
                                        // document.getElementById("newProfile").removeAttribute("disabled")
                                        document.getElementById("newApiKey").removeAttribute("disabled")
                                    }
                                }}
                                aria-disabled
                            >
                                <option value="">Chọn apiKey tại đây...</option>
                                {
                                    listApiKey.map((apiKey,index)=>{
                                        return(
                                            <option key={apiKey.id} value={apiKey.id}>{apiKey.apiKey}</option>
                                        )
                                    })

                                }
                            </select>

                        </div> */}
                        {/* <div className="form-group mb-5 d-flex ">
                            <label htmlFor="" className="col-form-label back-ground-label br">
                                <FontAwesomeIcon icon={faSimCard} style={{ color: "#ffffff", fontSize: 22 }} />
                            </label>
                            <select
                                className="form-control back-ground-input border border-dark"
                                id="select-sim"
                                onBlur={(e)=>{
                                    console.log(e.target.value)
                                    if(e.target.value!=="") document.getElementById("newProfile").setAttribute("disabled","true")
                                    else document.getElementById("newProfile").removeAttribute("disabled")
                                }}
                            >
                                <option value="">Chọn danh sách sim</option>
                                {
                                    listSim.map((sim,index)=>{
                                        return(
                                            <option key={index} value={sim.id}>{sim.name}</option>
                                        )
                                    })

                                }
                            </select>
                        </div> */}
                        <div className="btn-container">
                            <button type="submit" className="button-submit br" id="save-config"
                            onClick={(e)=>{
                                e.preventDefault();
                                let apiKey = document.getElementById("apiKey").value
                                let newApiKey = document.getElementById("newApiKey").value
                                // let idListSim = document.getElementById("select-sim").value
                                // let newProfilePath = document.getElementById("newProfile").value
                                if(newApiKey!="" ){
                                    this.addConfigInfo(newApiKey)
                                }
                                // else if(apiKey!=""){
                                //     let configData ={
                                //         apiKey :apiKey
                                //         // idListSim: idListSim
                                //     }
                                //     sessionStorage.setItem("configData",JSON.stringify(configData))
                                //     localStorage.clear()
                                //     alert("Lưu cấu hình thành công")
                                // }
                                else alert("Vui lòng điền đủ thông tin cấu hình")
                            }}
                            >
                                Lưu
                            </button>
                        </div>
                    </form>
                </main>
            </div>
        )
    }
}

export default Config