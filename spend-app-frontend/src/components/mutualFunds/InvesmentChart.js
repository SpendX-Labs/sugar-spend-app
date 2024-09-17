import React, { useState } from 'react';
import { LineChart, Line, YAxis, Tooltip, Legend, ResponsiveContainer, ReferenceLine } from 'recharts';
import { makeStyles } from '@mui/styles';
import axios from 'axios';
import { useCookies } from 'react-cookie';
import { COMMON_URL } from '../../constants/URL';

function formatDate(dateString) {
    const options = { year: 'numeric', month: '2-digit', day: '2-digit' };
    const date = new Date(dateString);
    return date.toLocaleDateString('en-GB', options);
}

const CustomTooltip = ({ active, payload }) => {
    if (active && payload && payload.length) {
        const data = payload[0].payload;
        return (
            <div className="custom-tooltip">
                <p>Day: {formatDate(data.date)}</p>
                <p>Invested Amount: {Math.round(data.investedAmount)}</p>
                <p>Current Amount: {Math.round(data.currentAmount)}</p>
            </div>
        );
    }
    return null;
};

const useStyles = makeStyles({
    chartContainer: {
        display: 'flex',
        flexDirection: 'column',
        alignItems: 'center',
        width: '100%',
        height: '70vh', // Adjust the height to fill the viewport
    },
    chartOptions: {
        display: 'flex',
        justifyContent: 'center',
        gap: '10px',
        margin: '20px', // Add margin for spacing
    },
    button: {
        borderRadius: '50%',
        border: '2px solid transparent',
        padding: '5px 15px',
        cursor: 'pointer',
    },
    activeButton: {
        borderColor: 'green',
    },
});

const InvestmentChart = () => {
    const [timeframe, setTimeframe] = useState('1M');
    const [cookies, _setCookie] = useCookies(['access_token']);
    const [data, setData] = useState([]);
    const classes = useStyles();

    const fetchData = async () => {
        try {
            axios.defaults.headers.common['Authorization'] = cookies['access_token'];
            const res = await axios.get(COMMON_URL + "app/get-line-chart");
            if (res.status === 200) {
                setData(res.data);
            }
        } catch (error) {
            // Handle errors if any of the API calls fail
            console.error('Error calling one or more APIs', error);
        }
    }

    React.useEffect(() => {
        fetchData();
    }, []);

    const filteredData =
        timeframe === '1M'
            ? data.slice(-30)
            : timeframe === '6M'
                ? data.slice(-180)
                : timeframe === '1Y'
                    ? data.slice(-365)
                    : data;

    return (
        <div className={classes.chartContainer}>
            <ResponsiveContainer width="100%" height="100%">
                <LineChart data={filteredData} margin={{ top: 5, right: 20, left: 10, bottom: 5 }}>
                    <YAxis />
                    <Tooltip content={<CustomTooltip />} />
                    <Legend />
                    <Line type="monotone" dataKey="investedAmount" stroke="#8884d8" />
                    <Line type="monotone" dataKey="currentAmount" stroke="#82ca9d" />
                    <ReferenceLine y={0} stroke="black" strokeWidth={1} />
                </LineChart>
            </ResponsiveContainer>

            <div className={classes.chartOptions}>
                <button
                    className={`${classes.button} ${timeframe === '1M' ? classes.activeButton : ''}`}
                    onClick={() => setTimeframe('1M')}
                >
                    1M
                </button>
                <button
                    className={`${classes.button} ${timeframe === '6M' ? classes.activeButton : ''}`}
                    onClick={() => setTimeframe('6M')}
                >
                    6M
                </button>
                <button
                    className={`${classes.button} ${timeframe === '1Y' ? classes.activeButton : ''}`}
                    onClick={() => setTimeframe('1Y')}
                >
                    1Y
                </button>
                <button
                    className={`${classes.button} ${timeframe === 'All' ? classes.activeButton : ''}`}
                    onClick={() => setTimeframe('All')}
                >
                    All
                </button>
            </div>
        </div>
    );
};

export default InvestmentChart;
