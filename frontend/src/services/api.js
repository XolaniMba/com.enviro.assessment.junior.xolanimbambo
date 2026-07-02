import axios from 'axios';

const api = axios.create({
  baseURL: 'http://localhost:8080/api',
  headers: {
    'Content-Type': 'application/json',
  },
});

// --- Investors ---
export const getAllInvestors = () => api.get('/investors');
export const getInvestorPortfolio = (investorId) => api.get(`/investors/${investorId}/portfolio`);

// --- Withdrawals ---
export const createWithdrawal = (withdrawalData) => api.post('/withdrawals', withdrawalData);
export const getWithdrawalHistory = (investorId) => api.get(`/withdrawals/investor/${investorId}`);
export const getCsvExportUrl = (investorId) =>
  `http://localhost:8080/api/withdrawals/investor/${investorId}/export`;

export default api;