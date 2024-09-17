import React, { useState } from 'react';
import { Tooltip, ResponsiveContainer, PieChart, Pie, Cell, Label } from 'recharts';
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

const PieChartView = (props) => {
    const [cookies] = useCookies(['access_token']);
    const [data, setData] = useState([]);
    const [totalAmount, setTotalAmount] = useState(0);
    const classes = useStyles();

    const fetchData = async () => {
        try {
            axios.defaults.headers.common['Authorization'] = cookies['access_token'];
            var reqMonth = "";
            if (!props.disableMonth)
                reqMonth = props.selectedMonth;
            const reqData = { budgetYear: props.selectedYear, budgetMonth: reqMonth };
            const res = await axios.post(COMMON_URL + "app/get-pie-chart", reqData);
            const totalData = await axios.post(COMMON_URL + "app/get-total-amount", reqData);
            if (res.status === 200 && totalData.status === 200) {
                setData(res.data);
                setTotalAmount(totalData.data["totalSpend"]);
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
                <PieChart width={730} height={200}>
                    <Pie
                        data={data}
                        dataKey="amount"
                        nameKey="dataKey"
                        cx="50%"
                        cy="50%"
                        innerRadius={60}
                        outerRadius={120}
                    >
                        {
                            data.map((entry, index) => (
                                <Cell key={`cell-${index}`} fill={`#${Math.floor(Math.random() * 16777215).toString(16)}`} />
                            ))
                        }
                        <Label
                            value={`${totalAmount}`}  // Customize the label as needed
                            position="center"
                            fill="#ff0000"  // Customize the color of the label
                            fontSize={24}  // Customize the font size of the label
                            fontWeight="bold"
                        />
                    </Pie>
                    <Tooltip />
                </PieChart>
            </ResponsiveContainer>

        </div>
    );
};

export default PieChartView;
