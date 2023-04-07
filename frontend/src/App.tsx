import React from "react";
import Card from '@mui/material/Card';
import Stack from '@mui/material/Stack';
import CardContent from '@mui/material/CardContent';
import Typography from '@mui/material/Typography';
import "./App.css";
import { fetchAnalyzed } from "./services/analyzeService";



function App() {
  let screenWidth = window.innerWidth;
  let colorArr = [
    'linear-gradient(to bottom, #f83000, #f86800)',
    'linear-gradient(to bottom, #f86800, #f89800)',
    'linear-gradient(to bottom, #f89800, #f8c800)',
    'linear-gradient(to bottom, #f8c800, #f8f800)',
    'linear-gradient(to bottom, #f8f800, #fcfcb2)',
  ]
  return (
    <Stack style={{
      display: 'flex',
      alignItems: 'center',
      justifyContent: 'center',
      backgroundColor: 'white',
      paddingLeft: 20,
      paddingTop: 20,
      paddingBottom: 20,
      paddingRight: 20
    }} 
    direction="column" spacing={2}>
      {fetchAnalyzed().map( (m, index) => {
              return (
                <Card sx={{ width: (screenWidth - 50) }}>
                  <CardContent sx={{ backgroundImage: colorArr[index],
                                     borderColor: 'black' }}>
                      <Typography gutterBottom variant="h5" component="div" color='black'>
                        {"Method: " + m.referencename}
                      </Typography>
                      <Typography variant="h5" component="div" color='black' marginTop={1} marginBottom={1}>
                        {"Package / Class: " + m.package}
                      </Typography>
                      <Typography variant="h5" component="div" color='black' marginTop={1}>
                        {"Calls: " + m.calls}
                      </Typography>
                  </CardContent>
                  <CardContent sx={{ backgroundColor: '#36454f' }}>
                    <Typography style={{whiteSpace: 'pre', fontFamily: 'courier'}} variant="body2" color="#ffeed0">
                        {"\n" +  m.source}
                    </Typography>
                  </CardContent>
              </Card>
              )
            }
          )
        }
    </Stack>
  );
}

export default App;
