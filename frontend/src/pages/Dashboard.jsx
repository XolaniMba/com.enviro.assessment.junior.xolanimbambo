import { useState, useEffect, useCallback } from 'react';
import {
  getAllInvestors,
  getInvestorPortfolio,
  getWithdrawalHistory,
  createWithdrawal,
  getCsvExportUrl,
} from '../services/api';

function Dashboard() {
  const [investors, setInvestors] = useState([]);
  const [selectedInvestorId, setSelectedInvestorId] = useState(null);
  const [portfolio, setPortfolio] = useState(null);
  const [history, setHistory] = useState([]);

  const [selectedProductId, setSelectedProductId] = useState('');
  const [amount, setAmount] = useState('');
  const [reason, setReason] = useState('');

  const [errorMessage, setErrorMessage] = useState('');
  const [successMessage, setSuccessMessage] = useState('');
  const [isSubmitting, setIsSubmitting] = useState(false);

  // Load the list of investors once, on first render, to populate the dropdown.
  useEffect(() => {
    getAllInvestors()
      .then((response) => {
        setInvestors(response.data);
        if (response.data.length > 0) {
          setSelectedInvestorId(response.data[0].id);
        }
      })
      .catch(() => setErrorMessage('Failed to load investors.'));
  }, []);

  // Reusable loader for portfolio + history, so we can call it again after a
  // successful withdrawal to refresh the screen with the new balance.
  const loadInvestorData = useCallback((investorId) => {
    if (!investorId) return;

    getInvestorPortfolio(investorId)
      .then((response) => setPortfolio(response.data))
      .catch(() => setErrorMessage('Failed to load portfolio.'));

    getWithdrawalHistory(investorId)
      .then((response) => setHistory(response.data))
      .catch(() => setErrorMessage('Failed to load withdrawal history.'));
  }, []);

  // Reload portfolio + history whenever the selected investor changes.
  useEffect(() => {
    loadInvestorData(selectedInvestorId);
  }, [selectedInvestorId, loadInvestorData]);

  const handleInvestorChange = (event) => {
    setSelectedInvestorId(Number(event.target.value));
    setSelectedProductId('');
    setErrorMessage('');
    setSuccessMessage('');
  };

  const handleWithdrawalSubmit = async (event) => {
    event.preventDefault();
    setErrorMessage('');
    setSuccessMessage('');

    if (!selectedProductId || !amount) {
      setErrorMessage('Please select a product and enter an amount.');
      return;
    }

    setIsSubmitting(true);
    try {
      await createWithdrawal({
        productId: Number(selectedProductId),
        amount: Number(amount),
        reason: reason || null,
      });
      setSuccessMessage('Withdrawal submitted successfully.');
      setAmount('');
      setReason('');
      loadInvestorData(selectedInvestorId); // refresh balance + history
    } catch (error) {
      // The backend's GlobalExceptionHandler always returns a JSON body
      // with a "message" field describing exactly what went wrong.
      const message = error.response?.data?.message || 'Something went wrong. Please try again.';
      setErrorMessage(message);
    } finally {
      setIsSubmitting(false);
    }
  };

  return (
    <div>
      <div className="card">
        <h2>Select Investor</h2>
        <select value={selectedInvestorId ?? ''} onChange={handleInvestorChange}>
          {investors.map((investor) => (
            <option key={investor.id} value={investor.id}>
              {investor.fullName} (age {investor.age})
            </option>
          ))}
        </select>
      </div>

      {portfolio && (
        <div className="card">
          <h2>Portfolio - {portfolio.fullName}</h2>
          <p>
            ID Number: {portfolio.idNumber} &nbsp;|&nbsp; Age: {portfolio.age} &nbsp;|&nbsp; Email: {portfolio.email}
          </p>
          <table>
            <thead>
              <tr>
                <th>Account Number</th>
                <th>Product Type</th>
                <th>Balance</th>
              </tr>
            </thead>
            <tbody>
              {portfolio.products.map((product) => (
                <tr key={product.id}>
                  <td>{product.accountNumber}</td>
                  <td>{product.productType.replaceAll('_', ' ')}</td>
                  <td>R {Number(product.balance).toLocaleString(undefined, { minimumFractionDigits: 2 })}</td>
                </tr>
              ))}
            </tbody>
          </table>
        </div>
      )}

      {portfolio && (
        <div className="card">
          <h2>Submit a Withdrawal</h2>

          {errorMessage && <div className="error-message">{errorMessage}</div>}
          {successMessage && <div className="success-message">{successMessage}</div>}

          <form onSubmit={handleWithdrawalSubmit}>
            <div className="form-group">
              <label htmlFor="product">Product</label>
              <select
                id="product"
                value={selectedProductId}
                onChange={(event) => setSelectedProductId(event.target.value)}
              >
                <option value="">-- Select a product --</option>
                {portfolio.products.map((product) => (
                  <option key={product.id} value={product.id}>
                    {product.accountNumber} ({product.productType.replaceAll('_', ' ')}) - R {Number(product.balance).toLocaleString()}
                  </option>
                ))}
              </select>
            </div>

            <div className="form-group">
              <label htmlFor="amount">Amount (R)</label>
              <input
                id="amount"
                type="number"
                min="0.01"
                step="0.01"
                value={amount}
                onChange={(event) => setAmount(event.target.value)}
                required
              />
            </div>

            <div className="form-group">
              <label htmlFor="reason">Reason (optional)</label>
              <input
                id="reason"
                type="text"
                value={reason}
                onChange={(event) => setReason(event.target.value)}
              />
            </div>

            <button type="submit" disabled={isSubmitting}>
              {isSubmitting ? 'Submitting...' : 'Submit Withdrawal'}
            </button>
          </form>
        </div>
      )}

      {portfolio && (
        <div className="card">
          <h2>Withdrawal History</h2>
          <button
            type="button"
            onClick={() => window.open(getCsvExportUrl(selectedInvestorId), '_blank')}
            style={{ marginBottom: '1rem' }}
          >
            Download CSV Statement
          </button>

          {history.length === 0 ? (
            <p>No withdrawals yet.</p>
          ) : (
            <table>
              <thead>
                <tr>
                  <th>Date</th>
                  <th>Account</th>
                  <th>Amount</th>
                  <th>Balance After</th>
                  <th>Status</th>
                  <th>Reason</th>
                </tr>
              </thead>
              <tbody>
                {history.map((item) => (
                  <tr key={item.id}>
                    <td>{new Date(item.requestedDate).toLocaleString()}</td>
                    <td>{item.accountNumber}</td>
                    <td>R {Number(item.amount).toLocaleString(undefined, { minimumFractionDigits: 2 })}</td>
                    <td>R {Number(item.balanceAfterWithdrawal).toLocaleString(undefined, { minimumFractionDigits: 2 })}</td>
                    <td>{item.status}</td>
                    <td>{item.reason || '-'}</td>
                  </tr>
                ))}
              </tbody>
            </table>
          )}
        </div>
      )}
    </div>
  );
}

export default Dashboard;