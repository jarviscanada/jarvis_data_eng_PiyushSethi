import { BrowserRouter, Navigate, Route, Routes } from 'react-router-dom';
// Currently, we should only have the Dashboard component
import Dashboard from './page/Dashboard/Dashboard';
import QuotePage from './page/QuotePage/QuotePage';
import TraderAccountPage from './page/TraderAccountPage/TraderAccountPage';
// Initialization of Router Component
export default function Router() {
    return (
        <BrowserRouter>
            <Routes>
                <Route path="/" element={<Navigate to="/traders" replace />} />
                <Route path="/traders" element={<Dashboard />} />
                <Route path='/quotes' element={<QuotePage />} />
                <Route path='/traders/:traderId' element={<TraderAccountPage />} />
            </Routes>
        </BrowserRouter>
    )
}