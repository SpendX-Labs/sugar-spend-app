import React, { useState } from 'react';
import { YAxis, Tooltip, ResponsiveContainer, BarChart, XAxis, Bar } from 'recharts';
import { makeStyles } from '@mui/styles';
import axios from 'axios';
import { useCookies } from 'react-cookie';
import { COMMON_URL } from '../../constants/URL';

const useStyles = makeStyles({
    chartContainer: {
        display: 'flex',
        flexDirection: 'column',
        alignItems: 'center',
        width: '100%',
        height: '50vh', // Adjust the height to fill the viewport
    },
});

const BarChartView = (props) => {
    const [cookies] = useCookies(['access_token']);
    const [data, setData] = useState([]);
    const classes = useStyles();

    const fetchData = async () => {
        try {
            axios.defaults.headers.common['Authorization'] = cookies['access_token'];
            var reqMonth = "";
            if (!props.disableMonth)
                reqMonth = props.selectedMonth;
            const reqData = { budgetYear: props.selectedYear, budgetMonth: reqMonth };
            const res = await axios.post(COMMON_URL + "app/get-budget-chart", reqData);
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
        // eslint-disable-next-line react-hooks/exhaustive-deps
    }, [props.selectedMonth, props.selectedYear, props.isSaved, props.disableMonth]);

    return (
        <div className={classes.chartContainer}>
            <ResponsiveContainer width="100%" height="100%">
                <BarChart width={630} height={200} data={data}>
                    <XAxis dataKey="dataKey" />
                    <YAxis />
                    <Tooltip cursor={{fill: 'rgba(255, 255, 255, 0.09)'}}/>
                    <Bar dataKey="amount" fill="#8884d8" />
                </BarChart>
            </ResponsiveContainer>

        </div>
    );
};

export default BarChartView;
