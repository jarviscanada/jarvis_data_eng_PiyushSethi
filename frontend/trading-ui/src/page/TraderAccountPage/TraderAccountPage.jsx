import React, { useEffect, useState } from 'react'
import './TraderAccountPage.scss'
import { traderAccountUrl, withdrawFundsUrl, depositFundsUrl } from '../../util/constants'
import 'antd/dist/antd.min.css'
import { Input, Modal, Button } from "antd";
import { useParams } from 'react-router-dom'
import NavBar from '../../component/NavBar/NavBar';
import axios from 'axios'

function TraderAccountPage() {
    const routeParams = useParams()
    const [state, setState] = useState({
        trader: {},
        traderId: null,
        isDepositModalVisible: false,
        isWithdrawModalVisible: false,
        depositFunds: null,
        withdrawFunds: null,
    });

    const fetchTrader = async (traderId) => {
        const response = await axios.get(traderAccountUrl + traderId)
        console.log(response)
        if (response) {
            setState(prev => ({
                ...prev,
                trader: response.data,
            }))
        }
    }
    useEffect(() => {
        if (routeParams && routeParams.traderId) {
            const traderId = routeParams.traderId
            setState(prev => ({
                ...prev,
                traderId,
            }))
            fetchTrader(traderId)
        }
    }, [])

    const showDepositModal = () => {
        setState(prev => ({
            ...prev,
            isDepositModalVisible: true,
        }))
    }
    const showWithdrawModal = () => {
        setState(prev => ({
            ...prev,
            isWithdrawModalVisible: true,
        }))
    }
    const handleDepositCancel = () => {
        setState(prev => ({
            ...prev,
            isDepositModalVisible: false,
            depositFunds: null,
        }))
    }
    const handleWithdrawCancel = () => {
        setState(prev => ({
            ...prev,
            isWithdrawModalVisible: false,
            withdrawFunds: null,
        }))
    }
    const handleDepositOk = async () => {
        const traderDepositUrl = `${depositFundsUrl}${state.traderId}/amount/${state.depositFunds}`;
        const response = await axios.put(traderDepositUrl)
        if (response) {
            await fetchTrader(state.traderId)
            setState(prev => ({
                ...prev,
                isDepositModalVisible: false,
            }))
        }
    }
    const handleWithdrawOk = async () => {
        const traderWithdrawUrl = withdrawFundsUrl + state.traderId + "/amount/" + state.withdrawFunds
        const response = await axios.put(traderWithdrawUrl)
        if (response) {
            await fetchTrader(state.traderId)
            setState(prev => ({
                ...prev,
                isWithdrawModalVisible: false,
            }))
        }
    }

    const onInputChange = (field, value) => {
        setState(prev => ({
            ...prev,
            [field]: value
        }))
    }
    return (
        <div className="trader-account-page">
            <NavBar />
            <div className="trader-account-page-content">
                <div className="title">
                    Trader Account
                </div>
                <div className="trader-cards">
                    <div className="trader-card">
                        <div className="info-row">
                            <div className="field">
                                <div className="content-heading">
                                    First Name
                                </div>
                                <div className="content">
                                    {state.trader.firstName}
                                </div>
                            </div>
                            <div className="field">
                                <div className="content-heading">
                                    Last Name
                                </div>
                                <div className="content">
                                    {state.trader.lastName}
                                </div>
                            </div>
                        </div>
                        <div className="info-row">
                            <div className="field">
                                <div className="content-heading">
                                    Email
                                </div>
                                <div className="content">
                                    {state.trader.email}
                                </div>
                            </div>
                        </div>
                        <div className="info-row">
                            <div className="field">
                                <div className="content-heading">
                                    Date of Birth
                                </div>
                                <div className="content">
                                    {state.trader.dob}
                                </div>
                            </div>
                            <div className="field">
                                <div className="content-heading">
                                    Country
                                </div>
                                <div className="content">
                                    {state.trader.country}
                                </div>
                            </div>
                        </div>
                    </div>
                    <div className="trader-card">
                        <div className="info-row">
                            <div className="field">
                                <div className="content-heading amount">
                                    Amount
                                </div>
                                <div className="content amount">
                                    {state.trader.amount}$
                                </div>
                            </div>
                        </div>
                    </div>
                    <div className="actions">
                        <Button onClick={showDepositModal}>Deposit Funds</Button>
                        <Modal title="Deposit Funds" okText="Submit" open={state.isDepositModalVisible} onOk={handleDepositOk} onCancel={handleDepositCancel}>
                            <div className="funds-form">
                                <div className="funds-field">
                                    <Input allowClear={false} placeholder="Funds" onChange={(event) => onInputChange("depositFunds", event.target.value)} />
                                </div>
                            </div>
                        </Modal>
                        {/* implement button for withdraw here */}
                        <Button onClick={showWithdrawModal}>Withdraw Funds</Button>
                        <Modal title="Withdraw Funds" okText="Submit" open={state.isWithdrawModalVisible} onOk={handleWithdrawOk} onCancel={handleWithdrawCancel}>
                            <div className="funds-form">
                                <div className="funds-field">
                                    <Input allowClear={false} placeholder="Funds" onChange={(event) => onInputChange("withdrawFunds", event.target.value)} />
                                </div>
                            </div>
                        </Modal>
                    </div>
                </div>
            </div>
        </div>
    );
}


export default TraderAccountPage