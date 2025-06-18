//it('should not call saveconfig when editMode is not false', () => {
//        const mockSaveConfig = jest.fn();
//
//  const component = new YourComponent({ saveconfig: mockSaveConfig });
//component.state = {
//editMode: 'true', // not 'false'
//currentUser: 'testUser',
//data: {},
//        };
//
//        component.yourSaveFunction();
//
//expect(mockSaveConfig).not.toHaveBeenCalled();
//});
//----------
//
//import React from 'react';
//import { render, act } from '@testing-library/react';
//import YourComponent from './YourComponent'; // replace with actual path
//
//        jest.useFakeTimers();
//
//describe('Save Config Functionality', () => {
//it('should handle successful save', async () => {
//        const mockSaveConfig = jest.fn((_, __, ___, callback) => {
//callback(); // simulate async callback
//    });
//
//            const mockDisplayAlertMsg = jest.fn();
//    const mockGetData = jest.fn();
//    const mockCloseModel = jest.fn();
//
//    const props = {
//saveconfig: mockSaveConfig,
//data: { status: 200 },
//        };
//
//        const component = new YourComponent(props);
//component.state = {
//editMode: 'false',
//currentUser: 'testUser',
//data: {},
//        };
//
//component.displayAlertMsg = mockDisplayAlertMsg;
//component.getData = mockGetData;
//component.closeAddAlertModel = mockCloseModel;
//component.setState = jest.fn();
//
//// Trigger the logic
//    component.yourSaveFunction(); // Replace with actual method name
//
//// Fast-forward timer
//act(() => {
//        jest.runAllTimers();
//    });
//
//expect(mockSaveConfig).toHaveBeenCalled();
//expect(component.setState).toHaveBeenCalledWith({ alertMsg: 'Saved data' });
//expect(mockDisplayAlertMsg).toHaveBeenCalledWith('success');
//expect(mockGetData).toHaveBeenCalled();
//expect(mockCloseModel).toHaveBeenCalled();
//  });
//
//it('should handle error response', async () => {
//        const mockSaveConfig = jest.fn((_, __, ___, callback) => {
//callback();
//    });
//
//            const mockDisplayAlertMsg = jest.fn();
//
//    const props = {
//saveconfig: mockSaveConfig,
//data: { status: 500, errors: 'Server Error' },
//        };
//
//        const component = new YourComponent(props);
//component.state = {
//editMode: 'false',
//currentUser: 'testUser',
//data: {},
//        };
//
//component.displayAlertMsg = mockDisplayAlertMsg;
//component.setState = jest.fn();
//
//    component.yourSaveFunction(); // Replace with actual method name
//
//act(() => {
//        jest.runAllTimers();
//    });
//
//expect(component.setState).toHaveBeenCalledWith({ alertMsg: 'Incorrect data' });
//expect(mockDisplayAlertMsg).toHaveBeenCalledWith('error');
//  });
//          });
