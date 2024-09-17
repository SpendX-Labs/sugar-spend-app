import React from 'react';
import Tabs from '@mui/material/Tabs';
import Tab from '@mui/material/Tab';
import TabPanel from '@mui/lab/TabPanel';
import { Box, Dialog, DialogContent } from '@mui/material';
import InvestMentChart from './InvesmentChart';
import { TabContext, TabList } from '@mui/lab';

const ProfileAnalysis = (props) => {
    const [value, setValue] = React.useState('1');

    const handleChange = (event, newValue) => {
        setValue(newValue);
    };

    return (
        <Dialog
            open={props.open}
            onClose={props.onClose}
            fullWidth
            maxWidth="lg"
            PaperProps={{
                style: {
                    width: "70%",
                    height: "90%",
                    maxHeight: "none",
                },
            }}
        >
            <DialogContent>
                <TabContext value={value}>
                    <Box sx={{ borderBottom: 1, borderColor: 'divider' }}>
                        <TabList onChange={handleChange} aria-label="lab API tabs example">
                            <Tab label="Investment Chart" value="1" />
                            <Tab label="Pie Chart" value="2" />
                        </TabList>
                    </Box>
                    <TabPanel value="1">
                        <InvestMentChart />
                    </TabPanel>
                    <TabPanel value="2">Item Two</TabPanel>
                </TabContext>
            </DialogContent>
        </Dialog>
    );
};

export default ProfileAnalysis;